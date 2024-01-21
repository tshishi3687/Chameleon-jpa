package Tshishi.Chameleon.Common.Interface;

import java.util.List;

public interface IdentifiedService<Dto, UUID> {

    Dto addEntity(Dto dto);

    Dto readEntity(UUID uuid);

    List<Dto> readAllEntity();

    Dto updateEntity(Dto dto, UUID uuid);

    void deleteEntity(UUID uuid);
}
