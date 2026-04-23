package itmo.lab.storage;

import itmo.lab.WorkersCollection;
import itmo.lab.data.Worker;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.fasterxml.jackson.databind.SerializationFeature;

import javax.xml.stream.XMLStreamException;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * XML хранилище коллекции.
 *
 * Сохраняет и загружает коллекцию в формате XML.
 * Использует Jackson для сериализации/десериализации.
 */
public class XmlFileStorage implements CollectionStorage {

    private final String filePath;
    private final XmlMapper xmlMapper;

    public XmlFileStorage(String filePath) {
        this.filePath = filePath;
        this.xmlMapper = new XmlMapper();
        this.xmlMapper.findAndRegisterModules();
        this.xmlMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Override
    public WorkersCollection load() throws DataAccessException {
        File file = new File(filePath);

        if (!file.exists()) {
            throw new DataAccessException("Файл не найден: " + filePath, new FileNotFoundException(filePath));
        }

        if (!file.canRead()) {
            throw new DataAccessException("Нет прав на чтение файла: " + filePath, new FileNotFoundException(filePath));
        }

        try (FileInputStream fis = new FileInputStream(file);
             InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8);
             BufferedReader reader = new BufferedReader(isr)) {

            StringBuilder xmlContent = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                xmlContent.append(line).append("\n");
            }

            return fromXml(xmlContent.toString(), filePath);

        } catch (IOException | XMLStreamException e) {
            throw new DataAccessException("Ошибка чтения XML: " + e.getMessage(), e);
        }
    }

    @Override
    public void save(WorkersCollection collection) throws DataAccessException {
        try {
            String xml = toXml(collection);

            try (FileOutputStream fos = new FileOutputStream(filePath);
                 OutputStreamWriter osw = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
                 BufferedWriter writer = new BufferedWriter(osw)) {

                writer.write(xml);
            }
        } catch (IOException | XMLStreamException e) {
            throw new DataAccessException("Ошибка записи XML: " + e.getMessage(), e);
        }
    }

    private String toXml(WorkersCollection collection) throws XMLStreamException, JsonProcessingException {
        WorkersWrapper wrapper = new WorkersWrapper(collection.getAll());
        return xmlMapper.writerWithDefaultPrettyPrinter().writeValueAsString(wrapper);
    }

    private WorkersCollection fromXml(String xml, String fileName) throws XMLStreamException, JsonProcessingException, DataAccessException {
        WorkersWrapper wrapper = xmlMapper.readValue(xml, WorkersWrapper.class);
        WorkersCollection collection = new WorkersCollection(fileName);

        if (wrapper.workerList != null) {
            for (Worker w : wrapper.workerList) {
                if (w.getId() <= 0) {
                    throw new DataAccessException("ID работника должен быть больше 0");
                }
                if (collection.containsKey(w.getId())) {
                    throw new DataAccessException("Дубликат ID: " + w.getId());
                }
                collection.insert(w.getId(), w);
            }
        }
        return collection;
    }

    @JacksonXmlRootElement(localName = "workers")
    private static class WorkersWrapper {
        @JacksonXmlProperty(localName = "worker")
        @JacksonXmlElementWrapper(useWrapping = false)
        public List<Worker> workerList;

        public WorkersWrapper() {}
        public WorkersWrapper(List<Worker> list) { this.workerList = list; }
    }
}