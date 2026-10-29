package academy.backend.market_pulse.repository.store;

import academy.backend.market_pulse.repository.store.impl.WatchlistStoreSkeleton;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.util.LinkedHashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MyWatchListStoreContractTest {
    protected WatchlistStore createStore() {
        return new WatchlistStoreSkeleton();
    }

    @Test
    void savedTickersComeBackInInsertionOrder(@TempDir Path tempDir) {
        WatchlistStore store = createStore();
        Path path = tempDir.resolve("watchlist.json");
        Set<String> tickers = new LinkedHashSet<>(Set.of());
        tickers.add("SBER");
        tickers.add("GAZP");

        store.save(path, tickers);
        Set<String> loaded = store.load(path);

        assertEquals(tickers, loaded);
    }

    @Test
    void loadingMissingFileReturnsEmptySet(@TempDir Path tempDir) {
        WatchlistStore store = createStore();
        Path path = tempDir.resolve("does-not-exist.json");

        assertTrue(store.load(path).isEmpty());
    }
}
