package Tshishi.Chameleon.Common.Interface;

import java.util.List;

public interface IdentifiedMapper<Dto, Entity> {

    Dto toDto(Entity entity);
    Entity toEntity(Dto dto);
    default List<Dto> toDtos(List<Entity> entities){
        return entities.stream().map(this::toDto).toList();
    }
}
