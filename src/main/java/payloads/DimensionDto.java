package payloads;

import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
@Builder
public class DimensionDto {

    private static Map<String, DimensionDto> dimensions = Map.of(
            "ecaflipus",  DimensionDto.builder().color(13490334).image("https://i.imgur.com/sLK4FmQ.png").build(),
            "enutrosor", DimensionDto.builder().color(16777064).image("https://i.imgur.com/ssMAcx3.png").build(),
            "srambad", DimensionDto.builder().color(3097192).image("https://i.imgur.com/jzpizTm.png").build(),
            "xelorium", DimensionDto.builder().color(7229801).image("https://i.imgur.com/vfQhS5D.png").build()
    );

    private int color;
    private String image;

    public static DimensionDto of(String dimension){
        return dimensions.get(dimension);
    }
}
