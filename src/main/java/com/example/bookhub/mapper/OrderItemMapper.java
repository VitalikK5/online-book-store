package com.example.bookhub.mapper;

import com.example.bookhub.dto.order.OrderItemResponseDto;
import com.example.bookhub.dto.order.OrderItemWithoutPriceResponseDto;
import com.example.bookhub.model.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderItemMapper {
    @Mapping(target = "bookId", source = "book.id")
    OrderItemResponseDto toDto(OrderItem orderItem);

    @Mapping(target = "bookId", source = "book.id")
    OrderItemWithoutPriceResponseDto toDtoWithoutPrice(OrderItem orderItem);
}
