package academy.backend.market_pulse.model;

import java.math.BigDecimal;
import java.math.RoundingMode;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

/**
 * Акция. В отличие от {@link Bond} и {@link Etf}, только у акций есть
 * дивидендная доходность — поэтому {@code getDividends} объявлен здесь,
 * а не в {@link Instrument}, и реализует {@link DividendPaying}.
 */
@Getter
public class Stock extends Instrument implements DividendPaying {

    private final String sector;

    /**
     * Дивидендная доходность в процентах — единственный числовой атрибут акции на этом этапе
     * проекта, используется в том числе как «аналог цены» в фильтрации ({@code PriceFilter}).
     */
    private final BigDecimal dividendYield;

    @JsonCreator
    public Stock(@JsonProperty("ticker") String ticker,@JsonProperty("name") String name,@JsonProperty("currency") Currency currency,
                 @JsonProperty("sector") String sector,@JsonProperty("dividendYield") BigDecimal dividendYield) {
        super(ticker, name, currency);
        this.sector = sector;
        this.dividendYield = dividendYield;
    }

    @Override
    public String getDescription() {
        return "Акция, сектор: " + sector;
    }

    @Override
    public String getType() {
        return "STOCK";
    }

    /**
     * Годовая дивидендная доходность в валюте инструмента: цена × доходность / 100.
     */
    @Override
    public BigDecimal getDividends(BigDecimal currentPrice) {
        return currentPrice.multiply(dividendYield)
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
    }
}
