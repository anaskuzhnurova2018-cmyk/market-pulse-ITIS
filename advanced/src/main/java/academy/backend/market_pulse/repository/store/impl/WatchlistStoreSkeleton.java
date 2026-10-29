package academy.backend.market_pulse.repository.store.impl;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import academy.backend.market_pulse.repository.store.WatchlistStore;

/**
 * Заготовка для самостоятельной работы (см. «Требования к файловому хранилищу.md», семинар 7) —
 * структура есть, решения нет. Формат файла — на ваш выбор (JSON, построчный текст и т.д.); главное,
 * чтобы {@code load(save(tickers))} возвращал тот же набор тикеров в том же порядке. Проверьте себя
 * тестом {@code WatchlistStoreContractTest} (замените в нём эталонную реализацию на свою).
 */
public class WatchlistStoreSkeleton implements WatchlistStore {

    @Override
    public void save(Path path, Set<String> tickers) {
        try {
            if (path.getParent() != null) {
                Files.createDirectories(path.getParent());
            }
            Files.write(path, tickers, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Set<String> load(Path path) {
        try {
            if (!Files.exists(path)) {
                return Set.of();
            }
            List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
            return new LinkedHashSet<>(lines);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
