package academy.backend.market_pulse.history.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.List;
import java.util.stream.Stream;

import academy.backend.market_pulse.history.PriceHistoryStore;
import academy.backend.market_pulse.history.PricePoint;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Заготовка для самостоятельной работы (см. «Требования к файловому хранилищу.md», семинар 7) —
 * структура есть, решения нет. Формат файла — на ваш выбор (бинарный, текстовый и т.д.); главное,
 * чтобы каждый {@link #append} создавал НОВЫЙ файл (не переписывал предыдущий) и
 * {@link #load(Path, String)} возвращал все сохранённые точки по тикеру в порядке возрастания
 * времени. Проверьте себя тестом {@code PriceHistoryStoreContractTest} (замените в нём эталонную
 * реализацию на свою).
 */
public class PriceHistoryStoreSkeleton implements PriceHistoryStore {

    private record PricePointDto(String ticker, long timestamp, BigDecimal price) {}

    private final ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    @Override
    public Path append(Path directory, PricePoint point) {
        try {
            Files.createDirectories(directory);
            String fileName = point.ticker() + "_" + point.timestamp().toEpochMilli() + ".json";
            Path filePath = directory.resolve(fileName);

            PricePointDto dto = new PricePointDto(point.ticker(), point.timestamp().toEpochMilli(), point.price());
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(filePath.toFile(), dto);
            return filePath;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<PricePoint> load(Path directory, String ticker) {
        try {
            if (!Files.exists(directory)) {
                return List.of();
            }

            try (Stream<Path> stream = Files.list(directory)) {
                return
                        stream.filter(file -> file.getFileName().toString().startsWith(ticker + "_"))
                                .sorted()
                                .map(path -> {
                                    try {
                                        PricePointDto dto = objectMapper.readValue(path.toFile(), PricePointDto.class);
                                        return new PricePoint(dto.ticker(), Instant.ofEpochMilli(dto.timestamp()), dto.price());
                                    } catch (IOException e) {
                                        throw new RuntimeException("Ошибка чтения файла: " + path, e);
                                    }
                                }).toList();
            }
        } catch (IOException e) {
            throw new RuntimeException("Не удалось прочитать директорию: " + directory, e);
        }
    }
}
