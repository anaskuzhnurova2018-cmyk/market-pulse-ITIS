package academy.backend.market_pulse.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class Etf extends Instrument {

    private final String trackingIndex;

    @JsonCreator
    public Etf(@JsonProperty("ticker") String ticker,@JsonProperty("name") String name,@JsonProperty("currency") Currency currency,@JsonProperty("trackingIndex") String trackingIndex) {
        super(ticker, name, currency);
        this.trackingIndex = trackingIndex;
    }

    @Override
    public String getDescription() {
        return "ETF, отслеживает индекс: " + trackingIndex;
    }

    @Override
    public String getType() {
        return "ETF";
    }
}
