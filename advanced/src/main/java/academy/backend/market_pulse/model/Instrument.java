package academy.backend.market_pulse.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Getter;

/**
 * Базовая абстракция финансового инструмента. Инкапсулирует общие для всех
 * инструментов данные (тикер, название, валюта) и защищает их инварианты
 * прямо в конструкторе.
 */
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Stock.class, name = "STOCK"),
        @JsonSubTypes.Type(value = Bond.class, name = "BOND"),
        @JsonSubTypes.Type(value = Etf.class, name = "ETF"),
})
@Getter
public abstract class Instrument {

    private final String ticker;
    private final String name;
    private final Currency currency;

    public Instrument(String ticker, String name, Currency currency) {
        // NOTICE: тикер — инвариант объекта; без проверки здесь инструмент
        // можно было бы создать в некорректном состоянии.
        if (ticker == null || ticker.isBlank()) {
            throw new IllegalArgumentException("Ticker cannot be blank");
        }
        this.ticker = ticker;
        this.name = name;
        this.currency = currency;
    }

    @JsonIgnore
    public abstract String getDescription();

    /**
     * Код типа инструмента ({@code STOCK}, {@code BOND}, {@code ETF}) — используется вместо
     * {@code getClass().getSimpleName()} там, где нужно узнать тип, не завязываясь на рефлексию.
     */
    public abstract String getType();

    @Override
    public String toString() {
        return ticker + " — " + name + " (" + getDescription() + ")";
    }
}
