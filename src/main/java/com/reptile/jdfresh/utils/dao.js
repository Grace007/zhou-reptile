{
    dataSource : {
        type : "org.apache.commons.dbcp.BasicDataSource",
        events : {
            depose : 'close'
        },
        fields : {
            driverClassName : 'com.mysql.jdbc.Driver',
            url : 'jdbc:mysql://127.0.0.1:3306/hcr_mysql',
            username : 'root',
            password : 'root'
        }
    }
}