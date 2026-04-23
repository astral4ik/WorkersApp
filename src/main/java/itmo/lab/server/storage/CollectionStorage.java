package itmo.lab.storage;

import itmo.lab.WorkersCollection;

/**
 * Интерфейс хранилища коллекции работников.
 *
 * Определяет методы для загрузки и сохранения коллекции.
 */
public interface CollectionStorage {

    /**
     * Загрузить коллекцию из хранилища.
     *
     * @return загруженная коллекция
     * @throws DataAccessException при ошибке чтения или отсутствии файла
     */
    WorkersCollection load() throws DataAccessException;

    /**
     * Сохранить коллекцию в хранилище.
     *
     * @param collection коллекция для сохранения
     * @throws DataAccessException при ошибке записи
     */
    void save(WorkersCollection collection) throws DataAccessException;
}