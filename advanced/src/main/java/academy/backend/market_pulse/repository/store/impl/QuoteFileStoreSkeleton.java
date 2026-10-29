package academy.backend.market_pulse.repository.store.impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import academy.backend.market_pulse.model.Quote;
import academy.backend.market_pulse.repository.store.QuoteFileStore;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Заготовка для самостоятельной работы (см. «Требования к файловому хранилищу.md», семинар 7) —
 * структура есть, решения нет. Формат файла — на ваш выбор (JSON, CSV, построчный текст); главное,
 * чтобы {@code load(save(quotes))} возвращал те же котировки. Проверьте себя тестом
 * {@code QuoteFileStoreContractTest} (замените в нём эталонную реализацию на свою).
 */
public class QuoteFileStoreSkeleton implements QuoteFileStore {

    private static final ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    @Override
    public void save(Path path, List<Quote> quotes) {
        try {
            if (path.getParent() != null) {
                Files.createDirectories(path.getParent());
            }
            mapper.writerWithDefaultPrettyPrinter().writeValue(path.toFile(), quotes);
        }  catch (IOException e) {
            throw new RuntimeException("Не удалось сохранить котировки в файл: " + path,e);
        }
    }

    @Override
    public List<Quote> load(Path path) {
        try {
            if (!Files.exists(path)) {
                return List.of();
            }
            return mapper.readValue(
                    path.toFile(),
                    new TypeReference<List<Quote>>() {
                    }
            );
        }  catch (IOException e) {
            throw new RuntimeException("Не удалось прочитать котировки из файла: " + path,e);
        }
    }
}
