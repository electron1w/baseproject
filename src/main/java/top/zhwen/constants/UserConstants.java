package top.zhwen.constants;

/**
 * Created by Zhw on 2017/8/17.
 */
public interface UserConstants {
    String algorithmName = "MD5";//密码加密算法
    Integer hashIterations = 2;//密码迭代次数
    Integer saltLength = 32;//盐值的长度
    byte state = 0;//账户添加状态默认值禁用
    boolean roleAvailable = true;//角色默认状态 有效
    Long first_order_menu = 0L;
    String the_menu_type_menu = "menu";
    String the_menu_type_button = "button";
    String adminUserName = "admin";//超级管理员登录名
    Integer restPwdDigit = 6;//重置密码位数

}
