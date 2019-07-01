//package com.webank.ai.fate.board.bootstrap;
//
//
////import com.alibaba.druid.support.http.StatViewServlet;
////import com.alibaba.druid.support.http.WebStatFilter;
////import com.codahale.metrics.MetricRegistry;
////import com.codahale.metrics.SharedMetricRegistries;
////import com.codahale.metrics.servlet.InstrumentedFilter;
////import com.codahale.metrics.servlets.MetricsServlet;
////import com.tencent.k12.crm.filter.CleanUserPermissionFilter;
////import com.tencent.k12.crm.interceptor.AutoLoadUserInterceptor;
////import com.tencent.k12.crm.interceptor.LogInterceptor;
////import com.tencent.k12.crm.interceptor.PtWtloginAuth;
////import com.tencent.k12.crm.interceptor.RoleCheckInterceptor;
////import com.tencent.k12.crm.qq.AuthUdpClient;
////import com.tencent.k12.crm.qq.CookieHeaderConfigs;
////import com.tencent.k12.crm.qq.Pair;
////import com.tencent.k12.crm.services.UserService;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.web.servlet.FilterRegistrationBean;
//import org.springframework.boot.web.servlet.ServletRegistrationBean;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//import javax.servlet.*;
//import java.util.ArrayList;
//
//@Configuration
//
//public class WebConfigration implements WebMvcConfigurer {
//
//
//    private final Logger logger = LoggerFactory.getLogger(WebConfigration.class);
//
//    @Autowired
//    ServletContext servletContext;
//
//    private MetricRegistry metricRegistry;
//
//    @Value("${metrics.registryName}")
//    String registryName;
//
//    @Autowired
//    protected CookieHeaderConfigs cookieHeaderConfigs;
//    @Autowired
//    protected AuthUdpClient client;
//    @Autowired
//    protected UserService userService;
////    @Resource
////    Filter shiroFilter;
//
//    @Value("${qq.login.local}")
//    boolean isLocal;
//
//
//
//    public  void addInterceptors(InterceptorRegistry registry) {
//
//        AutoLoadUserInterceptor autoLoadUserInterceptor  = new  AutoLoadUserInterceptor();
//
//        autoLoadUserInterceptor.setLocal(isLocal);
//
//        autoLoadUserInterceptor.setUserService(userService);
//
//        String ruleString = "fudao.qq.com:444,ke.qq.com:233";
//        //configs.getString("jungle.sdk.auth.webcore.domain_appids", "fudao.qq.com:444,ke.qq.com:233");
//        String rules[] = ruleString.split(",");
//        ArrayList<Pair<String, Short>> domainAppidRules = new ArrayList<Pair<String, Short>>(rules.length);
//        for (String rule : rules) {
//            String[] parts = rule.split(":");
//            domainAppidRules.add(new Pair<String, Short>(parts[0], Short.parseShort(parts[1])));
//        }
//
//
//        PtWtloginAuth    ptWtloginAuth = new  PtWtloginAuth();
//
//        ptWtloginAuth.setDomainAppidRules(domainAppidRules);
//
//        ptWtloginAuth.setCookieHeaderConfigs(cookieHeaderConfigs);
//
//
//        ptWtloginAuth.setClient(this.client);
//
//        ptWtloginAuth.setLocal(isLocal);
//
//
//        RoleCheckInterceptor  roleCheckInterceptor = new  RoleCheckInterceptor ();
//        registry.addInterceptor(new LogInterceptor()).addPathPatterns("/**");
//        registry.addInterceptor(ptWtloginAuth).addPathPatterns("/cgi-bin/crm/**").excludePathPatterns("/cgi-bin/crm/qidian/**");
//        registry.addInterceptor(autoLoadUserInterceptor).addPathPatterns("/cgi-bin/crm/**").excludePathPatterns("/cgi-bin/crm/qidian/**");
//        registry.addInterceptor(roleCheckInterceptor).addPathPatterns("/cgi-bin/crm/**").excludePathPatterns("/cgi-bin/crm/qidian/**");
//
//    }
//
//
//
//
//
//
//
////    @Bean
////    public RedisConnectionFactory redisConnectionFactory(){
////
////        LettuceConnectionFactory connectionFactory = new L5LettuceConnectionFactory();
////
////        return   connectionFactory;
////
////    }
//
//
//
////    @Bean
////    public HttpSessionStrategy httpSessionStrategy() {
////        return new CookieHttpSessionStrategy();
////    }
//
//
//    /**
//     *   这个很重要 ，千万不能注释掉 ，  因为权限放在了ThreadLocal中，如果没在请求结束之前释放，
//     *   有可能线程池中线程会被复用，导致权限错乱 kaideng
//     *
//     * @return
//     */
//    @Bean
//
//    public FilterRegistrationBean initUserServletFilterRegistration() {
//
//
//
//        FilterRegistrationBean registration = new FilterRegistrationBean();
//        CleanUserPermissionFilter cleanUserPermissionFilter = new CleanUserPermissionFilter();
//        registration.setFilter(cleanUserPermissionFilter);
//
//        registration.addUrlPatterns("/*");//设置过滤路径，/*所有路径
//       // registration.addInitParameter("name", "alue");//添加默认参数
//        registration.setName("CleanUserPermissionFilter");//设置优先级
//        registration.setOrder(Integer.MAX_VALUE);//设置优先级
//
//        return registration;
//    }
//
//
//
//
//
//
//
//
//
////    private void initDruid() {
////        ServletRegistration.Dynamic druidStatServlet =
////                servletContext.addServlet("druidStatServlet", new StatViewServlet());
////
////        druidStatServlet.addMapping("/druid/*");
////        druidStatServlet.setAsyncSupported(true);
////
////        // TODO 这里改为只监控 /api, /clientapi 的数据？
////        WebStatFilter druidWebStatFilter = new WebStatFilter();
////        FilterRegistration.Dynamic filter = servletContext.addFilter("druidWebStatFilter", druidWebStatFilter);
////        filter.setInitParameter("exclusions", "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*");
////        filter.addMappingForUrlPatterns(null, true, "/*");
////    }
//
//
//
//
//
//
//
//
//
//    @Bean
//    public FilterRegistrationBean initMetricsFilterRegistration() {
//        FilterRegistrationBean registration = new FilterRegistrationBean();
//
//
//
//        metricRegistry = SharedMetricRegistries.getOrCreate(registryName);
//
//        logger.debug("Initializing Metrics registries");
//        servletContext.setAttribute(InstrumentedFilter.REGISTRY_ATTRIBUTE, metricRegistry);
//        servletContext.setAttribute(MetricsServlet.METRICS_REGISTRY, metricRegistry);
//
//
//
//        registration.setFilter(new InstrumentedFilter());
//        registration.addUrlPatterns("/*");//设置过滤路径，/*所有路径
//        // registration.addInitParameter("name", "alue");//添加默认参数
//        registration.setName("metricFilter");//设置优先级
//        registration.setOrder(Integer.MIN_VALUE);//设置优先级
//        registration.setAsyncSupported(true);
//
//
//
//
//        return registration;
//    }
//
////    @Bean
////    public FilterRegistrationBean initAutoLoadUserFilterRegistration() {
////
////        FilterRegistrationBean registration = new FilterRegistrationBean();
////
////        AutoLoadUserFilter autoLoadUserFilter  = new  AutoLoadUserFilter();
////
////        autoLoadUserFilter.setLocal(isLocal);
////
////        autoLoadUserFilter.setUserService(userService);
////
////        registration.addUrlPatterns("/*");//设置过滤路径，/*所有路径
////        // registration.addInitParameter("name", "alue");//添加默认参数
////        registration.setName("AutoLoadUser");//设置优先级
////
////        registration.setFilter(autoLoadUserFilter);
////
////        registration.setOrder(Integer.MAX_VALUE-5);//设置优先级
////
////        //  registration.setAsyncSupported(true);
////        return registration;
////    }
//
//
////    @Bean
////
////    public FilterRegistrationBean initPtLoginFilterRegistration() {
////
////        FilterRegistrationBean registration = new FilterRegistrationBean();
////
////        String ruleString = "fudao.qq.com:444,ke.qq.com:233";
////                //configs.getString("jungle.sdk.auth.webcore.domain_appids", "fudao.qq.com:444,ke.qq.com:233");
////        String rules[] = ruleString.split(",");
////        ArrayList<Pair<String, Short>> domainAppidRules = new ArrayList<Pair<String, Short>>(rules.length);
////        for (String rule : rules) {
////            String[] parts = rule.split(":");
////            domainAppidRules.add(new Pair<String, Short>(parts[0], Short.parseShort(parts[1])));
////        }
////
////
////        PtWtloginAuth    ptWtloginAuth = new  PtWtloginAuth();
////
////        ptWtloginAuth.setDomainAppidRules(domainAppidRules);
////
////        ptWtloginAuth.setCookieHeaderConfigs(cookieHeaderConfigs);
////
////
////        ptWtloginAuth.setClient(this.client);
////
////        ptWtloginAuth.setLocal(isLocal);
////
////        registration.setFilter( ptWtloginAuth);
////
////
////        registration.addUrlPatterns("/*");//设置过滤路径，/*所有路径
////        // registration.addInitParameter("name", "alue");//添加默认参数
////        registration.setName("ptWtloginAuth");//设置优先级
////        registration.setOrder(Integer.MIN_VALUE);//设置优先级
////
////      //  registration.setAsyncSupported(true);
////        return registration;
////    }
//
//
//
//
//    @Bean
//    public ServletRegistrationBean    initMetricsServlet(){
//
//        ServletRegistrationBean  servletRegistrationBean =  new  ServletRegistrationBean( new MetricsServlet(),"/api/metrics/*");
//
//        servletRegistrationBean.setAsyncSupported(true);
//        servletRegistrationBean.setLoadOnStartup(2);
//
//        return  servletRegistrationBean;
//    }
//
//
//    @Bean
//    public FilterRegistrationBean filterRegistrationBean() {
//        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
//        filterRegistrationBean.setFilter(new WebStatFilter());
//        filterRegistrationBean.addUrlPatterns("/*");
//        filterRegistrationBean.addInitParameter("exclusions", "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*");
//        return filterRegistrationBean;
//    }
//
//
//    @Bean
//public ServletRegistrationBean druidServlet() {
//    logger.info("init Druid Servlet Configuration ");
//    ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean(new StatViewServlet(), "/druid/*");
////// IP白名单
////    servletRegistrationBean.addInitParameter("allow", "192.168.2.25,127.0.0.1");
////// IP黑名单(共同存在时，deny优先于allow)
////    servletRegistrationBean.addInitParameter("deny", "192.168.1.100");
////控制台管理用户
//    servletRegistrationBean.addInitParameter("loginUsername", "admin");
//    servletRegistrationBean.addInitParameter("loginPassword", "123456");
////是否能够重置数据 禁用HTML页面上的“Reset All”功能
//    servletRegistrationBean.addInitParameter("resetEnable", "false");
//    return servletRegistrationBean;
//    }
//
//
////    private void initMetricsFilter() {
////        metricRegistry = SharedMetricRegistries.getOrCreate(registryName);
////
////        logger.debug("Initializing Metrics registries");
////        servletContext.setAttribute(InstrumentedFilter.REGISTRY_ATTRIBUTE, metricRegistry);
////        servletContext.setAttribute(MetricsServlet.METRICS_REGISTRY, metricRegistry);
////
////        logger.debug("Registering Metrics Filter");
////        FilterRegistration.Dynamic metricsFilter =
////                servletContext.addFilter("webappMetricsFilter", new InstrumentedFilter());
////
////        EnumSet<DispatcherType> disps =
////                EnumSet.of(DispatcherType.REQUEST, DispatcherType.FORWARD, DispatcherType.ASYNC);
////
////        metricsFilter.addMappingForUrlPatterns(disps, true, "/*");
////        metricsFilter.setAsyncSupported(true);
////
////        logger.debug("Registering Metrics Servlet");
////        ServletRegistration.Dynamic metricsAdminServlet =
////                servletContext.addServlet("metricsServlet", new MetricsServlet());
////
////        metricsAdminServlet.addMapping("/api/metrics/*");
////        metricsAdminServlet.setAsyncSupported(true);
////        metricsAdminServlet.setLoadOnStartup(2);
////    }
////
////    private void initCsrfFilter() {
////        // 这里只拦截 /api/* 的请求，对于 /druid/不拦截
////        FilterRegistration.Dynamic filter = servletContext.addFilter("csrfFilter", new CsrfFilter());
////        filter.addMappingForUrlPatterns(null, true, "/api/*");
////    }
//
////    public void addReturnValueHandlers(List<HandlerMethodReturnValueHandler> handlers) {
////
////        handlers.add(new  ResponseHandlerMethodReturnValueHandler());
////    }
//}
