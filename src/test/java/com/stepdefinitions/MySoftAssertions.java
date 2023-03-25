package com.stepdefinitions;

import com.utilities.PlaywrightFactory;
import org.assertj.core.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//This class overrides onAssertionErrorCollected(), to be able to take screenShot after each softAssertion,
// if assertion will fail after assertAll()
public class MySoftAssertions extends SoftAssertions {

    Logger logger= LoggerFactory.getLogger(MySoftAssertions.class);
    public boolean isPass=true;
    @Override
    public void onAssertionErrorCollected(AssertionError assertionError) {
        PlaywrightFactory.takeScreenshot();
        isPass=false;
    }

    /*
    This method should be used for logging JUST AFTER an assertion, NOT for other logs, since it adds FAILED prefix to message
    and writes it with RED color. Until next assertion, isPass value stays as false.
     */
    public void logAssertion(String message){
        if(isPass){
            logger.info(message);
        }else{
            logger.warn("\u001B[31m" +"FAILED! -> "+message+ "\u001B[0m");//Writes message in RED color to the terminal
        }
    }

}
