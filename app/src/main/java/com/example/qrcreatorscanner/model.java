package com.example.qrcreatorscanner;
import com.google.zxing.Result;

public class model {
    private String Result;

    private String Datetime;
    public model()
    {

    }
    public model(String result,String datatime)
    {
       Result = result ;
       Datetime = datatime;
    }

    public String getDatetime() {
        return Datetime;
    }

    public void setDatetime(String datetime) {
        this.Datetime = datetime;
    }

    public String getResult() {
        return Result;
    }

    public void setResult(String result) {
        this.Result = result;
    }
    public String toString()
    {
        return   this.Result+"\n\n\n                                                   "+Datetime;
    }
}
