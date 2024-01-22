package org.prgrms.nabimarketbe.card.dto.request;

import java.util.List;

import javax.annotation.Nullable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.prgrms.nabimarketbe.annotation.ValidEnum;
import org.prgrms.nabimarketbe.card.entity.Card;
import org.prgrms.nabimarketbe.card.entity.TradeType;
import org.prgrms.nabimarketbe.cardimage.dto.request.CardImageCreateRequestDTO;
import org.prgrms.nabimarketbe.category.entity.Category;
import org.prgrms.nabimarketbe.category.entity.CategoryEnum;
import org.prgrms.nabimarketbe.item.entity.Item;
import org.prgrms.nabimarketbe.item.entity.PriceRange;
import org.prgrms.nabimarketbe.user.entity.User;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(title = "카드 등록 요청값 body data", description = "카드 등록 요청에 필요한 값들입니다.")
public record CardCreateRequestDTO(
    @Schema(title = "카드 제목", description = "카드 제목입니다.", example = "나비 카드 교환 원해요~")
    @NotBlank(message = "공백을 허용하지 않습니다.")
    @Size(max = 30)
    String cardTitle,

    @Schema(title = "썸네일 링크", description = "썸네일 이미지 링크입니다.", example = "https://xxx.amazonaws.com/xxx.png")
    @NotBlank(message = "공백을 허용하지 않습니다.")
    String thumbnail,

    @Schema(title = "아이템 이름", description = "아이템 이름입니다.", example = "나비 카드")
    @NotBlank(message = "공백을 허용하지 않습니다.")
    @Size(max = 30)
    String itemName,

    @Schema(title = "가격대", description = "상품의 가격대입니다.", example = "PRICE_RANGE_ONE")
    @ValidEnum(enumClass = PriceRange.class, message = "유효하지 않은 가격대입니다.")
    PriceRange priceRange,

    @Schema(title = "거래 방식", description = "카드의 거래 방식을 의미합니다.", example = "DIRECT_DEALING")
    @ValidEnum(enumClass = TradeType.class, message = "유효하지 않은 거래 방식입니다.")
    TradeType tradeType,

    @Schema(title = "거래 지역", description = "거래 하고자 하는 지역을 의미합니다.", example = "서울시 노원구")
    @NotNull
    @Size(max = 30)
    String tradeArea,

    @Schema(title = "카드 카테고리", description = "카드의 카테고리를 의미합니다.", example = "SHOES")
    @ValidEnum(enumClass = CategoryEnum.class, message = "유효하지 않은 카테고리입니다.")
    CategoryEnum category,

    @Schema(title = "카드 찔러보기 유무", description = "카드의 찔러보기 허용 여부를 의미합니다.", example = "true")
    @NotNull(message = "비울 수 없는 값입니다.")
    Boolean pokeAvailable,

    @Schema(title = "카드 본문 내용", description = "카드의 본문 내용을 의미합니다.", example = "나비 카드 교환원합니다. 상태 좋아요~!")
    @NotBlank(message = "공백을 허용하지 않습니다.")
    @Size(max = 255)
    String content,

    @Schema(title = "카드 이미지 링크 목록", description = "카드의 이미지 링크 목록")
    @Nullable
    List<CardImageCreateRequestDTO> images
) {
    public Item toItemEntity(Category category) {
        return Item.builder()
            .itemName(itemName)
            .priceRange(priceRange)
            .category(category)
            .build();
    }

    public Card toCardEntity(Item item, User user) {
        return Card.builder()
            .cardTitle(cardTitle)
            .thumbnail(thumbnail)
            .content(content)
            .tradeArea(tradeArea)
            .pokeAvailable(pokeAvailable)
            .tradeType(tradeType)
            .item(item)
            .user(user)
            .build();
    }
}
