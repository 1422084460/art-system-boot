package com.art.artadmin.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.art.artcommon.constant.R;
import com.art.artcommon.entity.IResult;
import com.art.artadmin.entity.User;
import com.art.artadmin.entity.User_log;
import com.art.artadmin.mapper.UserMapper;
import com.art.artcommon.utils.JWTUtils;
import com.art.artcommon.utils.RedisUtil;
import com.art.artcommon.utils.SpringContextHolder;
import com.art.artcommon.utils.Tools;
import com.art.artadmin.handler.Handler;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.StopWatch;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    private Handler handler;

    @Autowired
    private TransactionTemplate transactionTemplate;

    @Transactional(propagation = Propagation.REQUIRED)
    public List<User> QueryUser(){
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.like("name","a");
        List<User> users = userMapper.selectList(wrapper);
        stopWatch.stop();
        return users;
    }

    public void QueryUserTemplate(){
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        UpdateWrapper<User> wrapper = new UpdateWrapper<>();
        wrapper.like("name","a");
        wrapper.set("name","abc");
        //transactionTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);//????????????????????????????????????REQUIRED
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                userMapper.update(null,wrapper);
                //int i=1/0;
            }
        });
        stopWatch.stop();
        System.out.println(stopWatch.getTotalTimeMillis());
    }

    /**
     * ????????????
     * @param user ??????
     * @return int
     */
    @Transactional
    public int register(User user){
        int status = 0;
        try {
            user.setPassword(Tools.toMd5(user.getPassword()));
            status = userMapper.insert(user);
        }catch (Exception e){
            System.out.println("status:"+status);
        }
        return status;
    }

    /**
     * ?????? token
     * @param data ????????????
     * @param payload_args ??????
     * @return String
     */
    public String createToken(String data,String[] payload_args){
        Map map = JSON.parseObject(data);
        Map payload = new HashMap();
        for (String s : payload_args){
            payload.put(s,map.get(s));
        }
        return JWTUtils.getToken(payload);
    }

    /**
     * ????????????
     * @param data ????????????
     * @return IResult
     */
    public IResult login(JSONObject data){
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("email",data.getString("email"))
                .eq("password",Tools.toMd5(data.getString("password")));
        User user = userMapper.selectOne(wrapper);
        if (user!=null){
            handler = SpringContextHolder.getBean("directHandler");
            String date = Tools.date_To_Str(data.getLong("timestamp"));
            User_log userLog = new User_log()
                    .setUsername(user.getUsername())
                    .setEmail(user.getEmail())
                    .setLogin_time(date)
                    .setEvent(R.USER_LOGIN);
            handler.handler("batchSyncTask_user_log",JSONObject.toJSONString(userLog));
            JSONObject object = new JSONObject();
            user.setPassword("");
            object.put("user",user);
            return IResult.success(object);
        }
        return IResult.fail(null,"?????????????????????!",R.CODE_FAIL);
    }

    /**
     * ????????????
     * @param email ??????
     * @param newPassWord ?????????
     * @return int
     */
    @Transactional
    public int changePwd(String email,String newPassWord){
        UpdateWrapper<User> wrapper = new UpdateWrapper<>();
        wrapper.set("password",newPassWord).eq("email",email);
        return userMapper.update(null, wrapper);
    }
}
