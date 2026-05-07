package itmo.lab.server.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import itmo.lab.data.Coordinates;
import itmo.lab.data.Position;
import itmo.lab.data.Status;
import itmo.lab.data.Worker;
import itmo.lab.server.storage.DataAccessException;

/**
 * Репозиторий для CRUD-операций с работниками в таблице workers.
 */
public class WorkerRepository {

    private final OrganizationRepository orgRepo = new OrganizationRepository();

    /**
     * Загружает всех работников из DB вместе с данными об организациях.
     *
     * @return упорядоченная карта ID → Worker
     */
    public Map<Integer, Worker> loadAll() {
        Map<Integer, Worker> result = new LinkedHashMap<>();
        String sql = """
            SELECT w.*,
                   o.full_name, o.annual_turnover, o.employees_count,
                   o.street, o.loc_x, o.loc_y, o.loc_z
            FROM workers w
            LEFT JOIN organizations o ON w.organization_id = o.id
            ORDER BY w.id
            """;
        try (Connection con = DatabaseManager.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Worker w = mapRow(rs);
                result.put(w.getId(), w);
            }
        } catch (SQLException e) {
            throw new DataAccessException("Ошибка загрузки коллекции из DB", e);
        }
        return result;
    }

    /**
     * Вставляет нового работника в DB и возвращает сгенерированный ID.
     *
     * @param worker     работник для INSERT
     * @param ownerLogin Login владельца
     * @return сгенерированный ID
     */
    public long insert(Worker worker, String ownerLogin) {
        Long orgId = null;
        if (worker.getOrganization() != null) {
            orgId = orgRepo.insert(worker.getOrganization());
        }

        String sql = """
            INSERT INTO workers(
                name, coord_x, coord_y, creation_date, salary,
                start_date, position, status,
                organization_id, owner_login
            ) VALUES (?,?,?,?,?,?,?,?,?,?)
            RETURNING id
            """;
        try (Connection con = DatabaseManager.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, worker.getName());
            ps.setInt(2, worker.getCoordinates().getX());
            ps.setFloat(3, worker.getCoordinates().getY());
            ps.setTimestamp(4, Timestamp.valueOf(
                worker.getCreationDate()
            ));
            ps.setInt(5, worker.getSalary());
            if (worker.getStartDate() != null) {
                ps.setTimestamp(6, Timestamp.valueOf(worker.getStartDate()));
            } else {
                ps.setNull(6, Types.TIMESTAMP);
            }
            if (worker.getPosition() != null) {
                ps.setString(7, worker.getPosition().name());
            } else {
                ps.setNull(7, Types.VARCHAR);
            }
            if (worker.getStatus() != null) {
                ps.setString(8, worker.getStatus().name());
            } else {
                ps.setNull(8, Types.VARCHAR);
            }
            if (orgId != null) {
                ps.setLong(9, orgId);
            } else {
                ps.setNull(9, Types.BIGINT);
            }
            ps.setString(10, ownerLogin);

            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getLong("id");
            }
        } catch (SQLException e) {
            throw new DataAccessException("Ошибка INSERT работника", e);
        }
    }

    /**
     * Обновляет запись работника в DB, если пользователь является его владельцем.
     *
     * @param worker     работник с обновлёнными данными
     * @param ownerLogin Login владельца
     * @return {@code true}, если строка была обновлена
     */
    public boolean update(Worker worker, String ownerLogin) {
        Long existingOrgId = getOrganizationId(worker.getId());

        if (worker.getOrganization() != null) {
            if (existingOrgId != null) {
                orgRepo.update(existingOrgId, worker.getOrganization());
            } else {
                existingOrgId = orgRepo.insert(worker.getOrganization());
            }
        } else {
            if (existingOrgId != null) {
                orgRepo.delete(existingOrgId);
                existingOrgId = null;
            }
        }

        String sql = """
            UPDATE workers SET
                name=?, coord_x=?, coord_y=?, salary=?,
                start_date=?, position=?, status=?,
                organization_id=?
            WHERE id=? AND owner_login=?
            """;
        try (Connection con = DatabaseManager.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, worker.getName());
            ps.setInt(2, worker.getCoordinates().getX());
            ps.setFloat(3, worker.getCoordinates().getY());
            ps.setDouble(4, worker.getSalary());
            if (worker.getStartDate() != null) {
                ps.setTimestamp(5, Timestamp.valueOf(worker.getStartDate()));
            } else {
                ps.setNull(5, Types.TIMESTAMP);
            }
            if (worker.getPosition() != null) {
                ps.setString(6, worker.getPosition().name());
            } else {
                ps.setNull(6, Types.VARCHAR);
            }
            if (worker.getStatus() != null) {
                ps.setString(7, worker.getStatus().name());
            } else {
                ps.setNull(7, Types.VARCHAR);
            }
            if (existingOrgId != null) {
                ps.setLong(8, existingOrgId);
            } else {
                ps.setNull(8, Types.BIGINT);
            }
            ps.setLong(9, worker.getId());
            ps.setString(10, ownerLogin);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DataAccessException("Ошибка UPDATE работника", e);
        }
    }

    /**
     * Удаляет работника из DB, если пользователь является его владельцем.
     *
     * @param id         ID работника
     * @param ownerLogin Login владельца
     * @return {@code true}, если строка была удалена
     */
    public boolean delete(long id, String ownerLogin) {
        Long orgId = getOrganizationId(id);

        String sql = "DELETE FROM workers WHERE id=? AND owner_login=?";
        try (Connection con = DatabaseManager.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.setString(2, ownerLogin);
            boolean deleted = ps.executeUpdate() > 0;
            if (deleted && orgId != null) {
                orgRepo.delete(orgId);
            }
            return deleted;
        } catch (SQLException e) {
            throw new DataAccessException("Ошибка DELETE работника", e);
        }
    }

    /**
     * Удаляет всех работников указанного пользователя из DB.
     *
     * @param ownerLogin Login владельца
     * @return количество удалённых строк
     */
    public int deleteByOwner(String ownerLogin) {
        List<Long> orgIds = getOrganizationIdsByOwner(ownerLogin);

        String sql = "DELETE FROM workers WHERE owner_login=?";
        try (Connection con = DatabaseManager.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, ownerLogin);
            int count = ps.executeUpdate();
            for (Long orgId : orgIds) {
                orgRepo.delete(orgId);
            }
            return count;
        } catch (SQLException e) {
            throw new DataAccessException("Ошибка DELETE работников по владельцу", e);
        }
    }

    /**
     * Возвращает ID организации, привязанной к работнику, или {@code null}.
     */
    private Long getOrganizationId(long workerId) {
        String sql = "SELECT organization_id FROM workers WHERE id=?";
        try (Connection con = DatabaseManager.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, workerId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    long orgId = rs.getLong("organization_id");
                    return rs.wasNull() ? null : orgId;
                }
                return null;
            }
        } catch (SQLException e) {
            throw new DataAccessException("Ошибка получения ID организации", e);
        }
    }

    /**
     * Возвращает список ID организаций всех работников указанного владельца.
     */
    private List<Long> getOrganizationIdsByOwner(String ownerLogin) {
        String sql = "SELECT organization_id FROM workers WHERE owner_login=? AND organization_id IS NOT NULL";
        List<Long> ids = new ArrayList<>();
        try (Connection con = DatabaseManager.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, ownerLogin);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ids.add(rs.getLong("organization_id"));
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Ошибка получения ID организаций по владельцу", e);
        }
        return ids;
    }

    /**
     * Преобразует текущую строку ResultSet в объект Worker.
     *
     * @param rs результирующий набор, установленный на нужную строку
     * @return заполненный объект Worker
     */
    public Worker mapRow(ResultSet rs) throws SQLException {
        Worker w = new Worker();
        w.setId((int) rs.getLong("id"));
        w.setName(rs.getString("name"));

        Coordinates coords = new Coordinates();
        coords.setX(rs.getInt("coord_x"));
        coords.setY(rs.getFloat("coord_y"));
        w.setCoordinates(coords);

        w.setCreationDate(
            rs.getTimestamp("creation_date").toLocalDateTime()
        );

        w.setSalary(rs.getInt("salary"));

        Timestamp startDate = rs.getTimestamp("start_date");
        if (startDate != null) {
            w.setStartDate(startDate.toLocalDateTime());
        }

        String position = rs.getString("position");
        if (position != null) {
            w.setPosition(Position.valueOf(position));
        }

        String status = rs.getString("status");
        if (status != null) {
            w.setStatus(Status.valueOf(status));
        }

        String fullName = rs.getString("full_name");
        if (fullName != null) {
            w.setOrganization(orgRepo.mapRow(rs));
        }

        w.setOwnerLogin(rs.getString("owner_login"));
        return w;
    }
}
