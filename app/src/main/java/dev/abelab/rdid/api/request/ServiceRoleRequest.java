package dev.abelab.rdid.api.request;

import dev.abelab.rdid.enums.RoleEnum;
import dev.abelab.rdid.enums.ServiceEnum;
import dev.abelab.rdid.model.ServiceRoleModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ServiceRoleRequest {

    /**
     * サービスID
     */
    Integer serviceId;

    /**
     * ロールID
     */
    Integer roleId;

    public ServiceRoleModel convertToModel() {
        return ServiceRoleModel.builder() //
            .service(ServiceEnum.find(this.getServiceId())) //
            .role(RoleEnum.find(this.getRoleId())) //
            .build();
    }

}
