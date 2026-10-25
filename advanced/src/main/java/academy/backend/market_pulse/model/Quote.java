package academy.backend.market_pulse.model;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

/**
 * Рыночная котировка: инструмент + цена + изменение за период. Использует
 * агрегацию, а не наследование от {@link Stock} (см. «План семинара.md» —
 * эволюция StockSnapshot → Quote).
 */
@Getter
public class Quote {

    private final Instrument instrument;
    private final BigDecimal price;
    private final BigDecimal changePercent;

    @JsonCreator
    public Quote(@JsonProperty("instrument") Instrument instrument, @JsonProperty("price") BigDecimal price, @JsonProperty("changePercent") BigDecimal changePercent) {
        this.instrument = instrument;
        this.price = price;
        this.changePercent = changePercent;
    }

    public BigDecimal getDividends() {
        if (instrument instanceof Stock stock) {
            return stock.getDividends(this.price);
        }
        return BigDecimal.ZERO;
    }

    @Override
    public String toString() {
        String direction = switch (changePercent.signum()) {
            case 1 -> "▲";
            case -1 -> "▼";
            default -> "▬";
        };
        return instrument.getTicker() + ": " + price + " " + instrument.getCurrency()
                + " " + direction + " " + changePercent.abs() + "%";
    }
}
