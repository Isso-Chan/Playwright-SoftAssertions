package com.stepdefinitions;

import com.utilities.PlaywrightFactory;
import org.assertj.core.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//This class overrides onAssertionErrorCollected(), to be able to take screenShot after each softAssertion,
// if assertion will fail after assertAll()
public class MyLogsAndSoftAssertions extends SoftAssertions {

    Logger logger= LoggerFactory.getLogger(MyLogsAndSoftAssertions.class);
    private static boolean isPass=true;
    private String errorMessage;


    @Override
    public void onAssertionErrorCollected(AssertionError assertionError) {
        errorMessage=assertionError.toString().split("\\[")[1].split("]")[0];
        PlaywrightFactory.takeScreenshot();
        isPass=false;
    }

    /*
    This method should be used for logging JUST AFTER an assertion, NOT for other logs, since it adds FAILED prefix to message
    and writes it with RED color. Until next assertion, isPass value stays as false.
     */
    public void log(String message){
        if(isPass){
            logger.info(message);
        }else{
            logger.warn("\u001B[31m" +"FAILED! -> "+errorMessage+ "\u001B[0m");//Writes message in RED color to the terminal
            isPass=true;
        }

    }
    public void assertAll() {
        PlaywrightFactory.getThreadMap().put("inAssertAll",true);
        assertAll(this);
    }

}
