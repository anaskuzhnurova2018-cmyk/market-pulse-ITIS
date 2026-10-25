package academy.backend.market_pulse.history;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.time.Instant;

/** Одна историческая точка цены инструмента — то, из чего потом строится статистика. */
public record PricePoint(@JsonProperty("ticker") String ticker,@JsonProperty("timestamp") Instant timestamp,@JsonProperty("price")  BigDecimal price) {
    @JsonCreator
    public PricePoint {}
}
