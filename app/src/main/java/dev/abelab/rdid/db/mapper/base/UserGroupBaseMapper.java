package dev.abelab.rdid.db.mapper.base;

import dev.abelab.rdid.db.entity.UserGroup;
import dev.abelab.rdid.db.entity.UserGroupExample;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserGroupBaseMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_group
     *
     * @mbg.generated
     */
    long countByExample(UserGroupExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_group
     *
     * @mbg.generated
     */
    int deleteByExample(UserGroupExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_group
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_group
     *
     * @mbg.generated
     */
    int insert(UserGroup record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_group
     *
     * @mbg.generated
     */
    int insertSelective(UserGroup record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_group
     *
     * @mbg.generated
     */
    List<UserGroup> selectByExample(UserGroupExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_group
     *
     * @mbg.generated
     */
    UserGroup selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_group
     *
     * @mbg.generated
     */
    int updateByExampleSelective(@Param("record") UserGroup record, @Param("example") UserGroupExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_group
     *
     * @mbg.generated
     */
    int updateByExample(@Param("record") UserGroup record, @Param("example") UserGroupExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_group
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(UserGroup record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_group
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(UserGroup record);
}