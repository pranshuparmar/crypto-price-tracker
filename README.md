# crypto-price-tracker
Simple Cryptocurrency price tracker

application.properties file can be used to provide required configuration
    * price.max.bitcoin - this property can be used to set the maximum threshold price for bitcoin 
    * price.min.bitcoin - this property can be used to set the minimum threshold price for bitcoin
    * alert.email - this property can be used to set the email where email should be sent if price crosses either threshold

Command to run the application
    java -jar target/crypto-price-tracker-0.0.1.jar