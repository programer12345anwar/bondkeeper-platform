package com.bondkeeper.backend.mapper;

import com.bondkeeper.backend.dto.request.CategoryRequest;
import com.bondkeeper.backend.dto.request.ContactRequest;
import com.bondkeeper.backend.dto.request.InteractionRequest;
import com.bondkeeper.backend.dto.request.PriorityLevelRequest;
import com.bondkeeper.backend.dto.request.ReminderRequest;
import com.bondkeeper.backend.dto.request.UserRequest;
import com.bondkeeper.backend.dto.response.CategoryResponse;
import com.bondkeeper.backend.dto.response.ContactResponse;
import com.bondkeeper.backend.dto.response.InteractionResponse;
import com.bondkeeper.backend.dto.response.PriorityLevelResponse;
import com.bondkeeper.backend.dto.response.ReminderResponse;
import com.bondkeeper.backend.dto.response.UserResponse;
import com.bondkeeper.backend.entity.Category;
import com.bondkeeper.backend.entity.Contact;
import com.bondkeeper.backend.entity.Interaction;
import com.bondkeeper.backend.entity.PriorityLevel;
import com.bondkeeper.backend.entity.Reminder;
import com.bondkeeper.backend.entity.User;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface EntityMapper {

    UserResponse toUserResponse(User user);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    User toUser(UserRequest request);

    @Mapping(target = "userId", source = "user.id")
    CategoryResponse toCategoryResponse(Category category);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Category toCategory(CategoryRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateCategory(CategoryRequest request, @MappingTarget Category category);

    @Mapping(target = "userId", source = "user.id")
    PriorityLevelResponse toPriorityLevelResponse(PriorityLevel priorityLevel);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    PriorityLevel toPriorityLevel(PriorityLevelRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updatePriorityLevel(PriorityLevelRequest request, @MappingTarget PriorityLevel priorityLevel);

    @Mapping(target = "categoryId", source = "category.id")
    @Mapping(target = "priorityLevelId", source = "priorityLevel.id")
    @Mapping(target = "userId", source = "user.id")
    ContactResponse toContactResponse(Contact contact);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "priorityLevel", ignore = true)
    @Mapping(target = "interactions", ignore = true)
    @Mapping(target = "reminders", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Contact toContact(ContactRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "priorityLevel", ignore = true)
    @Mapping(target = "interactions", ignore = true)
    @Mapping(target = "reminders", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateContact(ContactRequest request, @MappingTarget Contact contact);

    @Mapping(target = "contactId", source = "contact.id")
    InteractionResponse toInteractionResponse(Interaction interaction);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "contact", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Interaction toInteraction(InteractionRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "contact", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateInteraction(InteractionRequest request, @MappingTarget Interaction interaction);

    @Mapping(target = "contactId", source = "contact.id")
    ReminderResponse toReminderResponse(Reminder reminder);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "contact", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Reminder toReminder(ReminderRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "contact", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateReminder(ReminderRequest request, @MappingTarget Reminder reminder);
}
