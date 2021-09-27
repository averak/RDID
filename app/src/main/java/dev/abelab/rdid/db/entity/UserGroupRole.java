package dev.abelab.rdid.db.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserGroupRole {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user_group_role.group_id
     *
     * @mbg.generated
     */
    private Integer groupId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user_group_role.client_id
     *
     * @mbg.generated
     */
    private Integer clientId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user_group_role.role_id
     *
     * @mbg.generated
     */
    private Integer roleId;
}