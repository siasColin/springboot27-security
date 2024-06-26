package cn.net.ssd.model.sysManage;

import cn.net.ssd.common.helper.LongJsonDeserializer;
import cn.net.ssd.common.helper.LongJsonSerializer;
import cn.net.ssd.model.common.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * 用户表 sys_user
 * 为了整合SpringSecurity，需要实现 UserDetails
 *
 * @author sxf
 * date:2020/03/04 17:48
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SysUser implements Serializable, UserDetails {
    /**
     * 串行版本ID
     */
    private static final long serialVersionUID = -6216434461016373495L;

    /**
     * 主键ID
     */
    @JsonSerialize(using = LongJsonSerializer.class)
    @JsonDeserialize(using = LongJsonDeserializer.class)
    @ApiModelProperty(hidden = true)
    private Long id;

    /**
     * 登录名
     */
    @ApiModelProperty(value = "账号", hidden = true)
    private String loginName;

    /**
     * 登录密码
     */
    @ApiModelProperty(value = "密码", hidden = true)
    @JsonIgnore
    private String password;

    /**
     * 是否管理员(0 非管理用户,1管理员)
     * 从角色权限中判断，这里弃用
     */
    /*private Integer adminFlag;*/

    /**
     * 姓名
     */
    @ApiModelProperty(value = "姓名", required = true)
    private String userName;

    /**
     * 性别(0 男，1 女)
     */
    @ApiModelProperty(value = "性别(0 男，1 女)", required = true, example = "0")
    private Integer userGender;

    /**
     * 电话号码
     */
    @ApiModelProperty(value = "电话号码", required = true)
    private String phoneNumber;

    /**
     * 邮箱
     */
    @ApiModelProperty(value = "邮箱", required = false)
    private String userEmail;

    /**
     * 关联机构表机构编码
     */
    @ApiModelProperty(value = "所属机构编码", required = true)
    private String orgCode;

    /**
     * 最后登录时间
     */
    @ApiModelProperty(hidden = true)
    private Date lastLoginTime;

    /**
     * 最后登录IP
     */
    @ApiModelProperty(hidden = true)
    private String lastLoginIp;

    /**
     * 用户状态(0 正常，2 禁用， 3 过期， 4 锁定)
     */
    @ApiModelProperty(value = "用户状态(0 正常，2 禁用， 3 过期， 4 锁定)", required = true, example = "0")
    private Integer userStatus;

    /**
     * 创建人
     */
    @ApiModelProperty(hidden = true)
    private String createUser;

    /**
     * 创建时间
     */
    @ApiModelProperty(hidden = true)
    private Date createTime;

    /**
     * 存储角色信息
     */
    private List<Role> roles;
    /**
     * 存储所属机构信息
     */
    private SysOrg sysOrg;

    /**
     * 所属机构名称
     */
    @ApiModelProperty(hidden = true)
    private String orgName;

    /**
     * 头像
     */
    @ApiModelProperty(value = "头像", required = false)
    private String headImg = "image/boy-01.png";


    /**
     * 获取 主键ID sys_user.id
     *
     * @return 主键ID
     */
    public final Long getId() {
        return id;
    }

    /**
     * 设置 主键ID sys_user.id
     *
     * @param id 主键ID
     */
    public final void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取 登录名 sys_user.login_name
     *
     * @return 登录名
     */
    public final String getLoginName() {
        return loginName;
    }

    /**
     * 设置 登录名 sys_user.login_name
     *
     * @param loginName 登录名
     */
    public final void setLoginName(String loginName) {
        this.loginName = loginName == null ? null : loginName.trim();
    }

    @JsonIgnore
    private Collection<? extends GrantedAuthority> authorities;

    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    /**
     * 获取 登录密码 sys_user.password
     *
     * @return 登录密码
     */
    @Override
    public final String getPassword() {
        return password;
    }

    @Override
    @JsonIgnore
    @ApiModelProperty(hidden = true)
    public String getUsername() {
        return loginName;
    }

    /**
     * 是否未过期
     *
     * @return
     */
    @Override
    @JsonIgnore
    @ApiModelProperty(hidden = true)
    public boolean isAccountNonExpired() {
        if (this.userStatus == 3) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 是否未被锁定
     *
     * @return
     */
    @Override
    @JsonIgnore
    @ApiModelProperty(hidden = true)
    public boolean isAccountNonLocked() {
        if (this.userStatus == 4) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 密码是否未过期
     *
     * @return
     */
    @Override
    @JsonIgnore
    @ApiModelProperty(hidden = true)
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * 是否可用
     *
     * @return
     */
    @Override
    @JsonIgnore
    @ApiModelProperty(hidden = true)
    public boolean isEnabled() {
        if (this.userStatus == 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 设置 登录密码 sys_user.password
     *
     * @param password 登录密码
     */
    public final void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    /**
     * 获取 是否管理员(0 非管理用户,1管理员) sys_user.admin_flag
     * @return 是否管理员(0 非管理用户, 1管理员)
     */
    /*public final Integer getAdminFlag() {
        return adminFlag;
    }*/

    /**
     * 设置 是否管理员(0 非管理用户,1管理员) sys_user.admin_flag
     * @param adminFlag 是否管理员(0 非管理用户,1管理员)
     */
    /*public final void setAdminFlag(Integer adminFlag) {
        this.adminFlag = adminFlag;
    }*/

    /**
     * 获取 姓名 sys_user.user_name
     *
     * @return 姓名
     */
    public final String getUserName() {
        return userName;
    }

    /**
     * 设置 姓名 sys_user.user_name
     *
     * @param userName 姓名
     */
    public final void setUserName(String userName) {
        this.userName = userName == null ? null : userName.trim();
    }

    /**
     * 获取 性别(0 男，1 女) sys_user.user_gender
     *
     * @return 性别(0 男 ， 1 女)
     */
    public final Integer getUserGender() {
        return userGender;
    }

    /**
     * 设置 性别(0 男，1 女) sys_user.user_gender
     *
     * @param userGender 性别(0 男，1 女)
     */
    public final void setUserGender(Integer userGender) {
        this.userGender = userGender;
    }

    /**
     * 获取 电话号码 sys_user.phone_number
     *
     * @return 电话号码
     */
    public final String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * 设置 电话号码 sys_user.phone_number
     *
     * @param phoneNumber 电话号码
     */
    public final void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber == null ? null : phoneNumber.trim();
    }

    /**
     * 获取 邮箱 sys_user.user_email
     *
     * @return 邮箱
     */
    public final String getUserEmail() {
        return userEmail;
    }

    /**
     * 设置 邮箱 sys_user.user_email
     *
     * @param userEmail 邮箱
     */
    public final void setUserEmail(String userEmail) {
        this.userEmail = userEmail == null ? null : userEmail.trim();
    }

    /**
     * 获取 关联机构表机构编码 sys_user.org_code
     *
     * @return 关联机构表机构编码
     */
    public final String getOrgCode() {
        return orgCode;
    }

    /**
     * 设置 关联机构表机构编码 sys_user.org_code
     *
     * @param orgCode 关联机构表机构编码
     */
    public final void setOrgCode(String orgCode) {
        this.orgCode = orgCode == null ? null : orgCode.trim();
    }

    /**
     * 获取 最后登录时间 sys_user.last_login_time
     *
     * @return 最后登录时间
     */
    public final Date getLastLoginTime() {
        return lastLoginTime;
    }

    /**
     * 设置 最后登录时间 sys_user.last_login_time
     *
     * @param lastLoginTime 最后登录时间
     */
    public final void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    /**
     * 获取 最后登录IP sys_user.last_login_ip
     *
     * @return 最后登录IP
     */
    public final String getLastLoginIp() {
        return lastLoginIp;
    }

    public Integer getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(Integer userStatus) {
        this.userStatus = userStatus;
    }

    /**
     * 设置 最后登录IP sys_user.last_login_ip
     *
     * @param lastLoginIp 最后登录IP
     */
    public final void setLastLoginIp(String lastLoginIp) {
        this.lastLoginIp = lastLoginIp == null ? null : lastLoginIp.trim();
    }

    /**
     * 获取 创建人 sys_user.create_user
     *
     * @return 创建人
     */
    public final String getCreateUser() {
        return createUser;
    }

    /**
     * 设置 创建人 sys_user.create_user
     *
     * @param createUser 创建人
     */
    public final void setCreateUser(String createUser) {
        this.createUser = createUser == null ? null : createUser.trim();
    }

    /**
     * 获取 创建时间 sys_user.create_time
     *
     * @return 创建时间
     */
    public final Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置 创建时间 sys_user.create_time
     *
     * @param createTime 创建时间
     */
    public final void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public SysOrg getSysOrg() {
        return sysOrg;
    }

    public void setSysOrg(SysOrg sysOrg) {
        this.sysOrg = sysOrg;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        if (headImg != null && !headImg.trim().equals("")) {
            this.headImg = headImg;
        }
    }

    @Override
    public final String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append(", id=").append(id);
        sb.append(", loginName=").append(loginName);
        sb.append(", password=").append(password);
        /*sb.append(", adminFlag=").append(adminFlag);*/
        sb.append(", userName=").append(userName);
        sb.append(", userGender=").append(userGender);
        sb.append(", phoneNumber=").append(phoneNumber);
        sb.append(", userEmail=").append(userEmail);
        sb.append(", orgCode=").append(orgCode);
        sb.append(", lastLoginTime=").append(lastLoginTime);
        sb.append(", lastLoginIp=").append(lastLoginIp);
        sb.append(", createUser=").append(createUser);
        sb.append(", createTime=").append(createTime);
        sb.append("]");
        return sb.toString();
    }
}