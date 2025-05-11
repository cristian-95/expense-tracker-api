package com.cristian.ExpenseTrackerAPI.mapper;

import com.cristian.ExpenseTrackerAPI.model.Category;
import com.cristian.ExpenseTrackerAPI.model.dto.CreateExpenseDTO;
import com.cristian.ExpenseTrackerAPI.model.dto.ExpenseResponseDTO;
import com.cristian.ExpenseTrackerAPI.model.dto.UpdateExpenseDTO;
import com.cristian.ExpenseTrackerAPI.model.entity.Expense;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ExpenseMapper {

    ExpenseMapper INSTANCE = Mappers.getMapper(ExpenseMapper.class);

    @Mapping(target = "user", ignore = true)
    Expense toEntity(CreateExpenseDTO dto);

    @Mapping(target = "username", source = "user.username")
    ExpenseResponseDTO toResponseDTO(Expense expense);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "category", source = "category")
    void updateFromDTO(UpdateExpenseDTO dto, @MappingTarget Expense entity);

    @Named("stringToCategory")
    default Category stringToCategory(String category) {
        return Category.fromString(category);
    }
}
