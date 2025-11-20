package com.springexample.irrigationagriculture.dto.responseDto;


public class UserToken {

    private String token;

   public static class Builder{

       private String token;

   public Builder(String token){
       super();
       this.token=token;
   }

   public UserToken build(){
       return new UserToken(this);
   }

   }

   public UserToken(Builder builder){
       token=builder.token;
   }

    public String getToken() {
        return token;
    }
    public void setToken(String token) {}
}
