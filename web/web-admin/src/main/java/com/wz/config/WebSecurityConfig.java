package com.wz.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


@Configuration      //声明当前类是一个配置类(当前类需要被扫描：spring-mvc中)
@EnableWebSecurity //@EnableWebSecurity是开启SpringSecurity的自动配置，会生成一个登录页面
@EnableGlobalMethodSecurity(prePostEnabled = true)  //开启Controller中方法的权限控制
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {


    // //在内存中设置一个认证的用户名和密码
    // @Override
    // protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    //     auth.inMemoryAuthentication()
    //             .withUser("admin")
    //             .password(new BCryptPasswordEncoder().encode("123456"))
    //             .roles("");
    // }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // //必须调用父类的方法；否则不需要认证即可访问,除非当前方法配置了认证授权的过程
        // super.configure(http);
        // //允许iframe嵌套显示
        // //http.headers().frameOptions().disable();
        // //允许iframe显示
        http.headers().frameOptions().sameOrigin();
        //登录设置
        http
                .authorizeRequests()
                .antMatchers("/static/**", "/login").permitAll()  //允许匿名用户访问的路径
                .anyRequest().authenticated()    // 其它页面全部需要验证
                .and()
                .formLogin()
                .loginPage("/login")    //用户未登录时，访问任何需要权限的资源都转跳到该路径，即登录页面，此时登陆成功后会继续跳转到第一次访问的资源页面（相当于被过滤了一下）
                .defaultSuccessUrl("/") //登录认证成功后默认转跳的路径，意思时admin登录后也跳转到/user
                .and()
                .logout()
                .logoutUrl("/logout")   //退出登陆的路径，指定spring security拦截的注销url,退出功能是security提供的
                .logoutSuccessUrl("/login")//用户退出后要被重定向的url
                .and()
                .csrf().disable();//关闭跨域请求伪造功能

        //配置自定义的无权限访问的处理器
        http.exceptionHandling().accessDeniedHandler(new CustomAccessDeniedHandler());
    }

    //创建一个密码加密器(相当于对应的解密器)放到IOC容器中
    @Bean //相当于bean标签
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
