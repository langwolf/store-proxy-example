# store-proxy-example
### 1. Make Jar

mvn package

### 2. Open

@EnableRedisProxy

### 3. Use

```
@Resource(name = IRedisDao.COMMON_REDIS)
private IRedisDao redisDao
```
