package my.junw.usercenter;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.nio.file.Paths;
import java.util.Collections;

/**
 * mp-plus 代码生成器
 */
public class CodeGenerator {

    private static String moduleName = "";    //模块名称  spaceTime
    private static String author = "ld";    //作者
    private static String projectPath = "D:\\A-Project\\A-ZSXQ\\Junw-UserCenter\\junw-user-center-server";    //代码生成路径
    private static String[] tableNames = new String[] {"user"};    //需要生成代码的数据库表名
    private static String[] tablePrefix = new String[] { "TR_", "TS_", "V_", "T_" , "TT_", "czmk_", "view_", "hsmk_", "z_"};    //表前缀

    private static String url = "jdbc:mysql://localhost:3308/junw";    //数据源url
    private static String username = "root";    //数据源连接用户名
    private static String password = "123456";    //数据源连接密码


    public static void main(String[] args) {
        FastAutoGenerator.create(url, username, password)
                .globalConfig(builder -> builder
                        .author(author)
                        .outputDir(Paths.get(projectPath) + "/" + moduleName + "/src/main/java")
                        .commentDate("yyyy-MM-dd")
                        .disableOpenDir()
                        // .enableSwagger()
                        .dateType(DateType.ONLY_DATE)
                )
                .packageConfig(builder -> builder
                        // .moduleName("")
                        .parent("my.junw.usercenter")
                        .entity("entity")
                        .mapper("dao")
                        .service("service")
                        .serviceImpl("service.Impl")
                        .controller("controller")
                        .pathInfo(Collections.singletonMap(OutputFile.xml,projectPath + "/src/main/resources/mybatis/mapper/" + moduleName+ "/mysql/"  ))
                )
                .strategyConfig(builder -> builder

                        .addInclude(tableNames)  // 表名s
                        .addTablePrefix(tablePrefix)  // 表名-前缀s

                        .entityBuilder() // --> 实体类
                        .enableLombok() // 开启 Lombok 模型
                        .enableChainModel() // 开启链式模型
                        .enableTableFieldAnnotation() // 启用字段注解
                        .naming(NamingStrategy.underline_to_camel)
                        .enableColumnConstant() // 开启生成字段常量
                        .columnNaming(NamingStrategy.underline_to_camel)
                        .enableFileOverride()  // 覆盖已生成文件
                        // .idType(IdType.ASSIGN_UUID)
                        .formatFileName("%sEO")

                        .controllerBuilder()// --> 控制器
                        .enableHyphenStyle()
                        .enableRestStyle()
                        .formatFileName("%sEOController")
                        .enableFileOverride()

                        .serviceBuilder()// --> 业务层
                        .formatServiceFileName("I%sEOService")
                        .formatServiceImplFileName("%sEOServiceImpl")
                        .enableFileOverride()

                        .mapperBuilder()
                        .superClass(BaseMapper.class)
                        .enableBaseResultMap()
                        .enableBaseColumnList()
                        .formatMapperFileName("%sEODao")
                        .formatXmlFileName("%sEOMapper")
                        .enableFileOverride()
                )
                .templateEngine(new FreemarkerTemplateEngine())
                .execute();

    }

}
