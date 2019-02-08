package com.tw.deusemar.gtc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import com.tw.deusemar.gtc.controller.ConferenceScheduler;
import com.tw.deusemar.gtc.entity.Conference;
import com.tw.deusemar.gtc.util.Logger;

public class ConferenceSchedulerMain {
    public static void main(String[] args) {
        Logger logger = Logger.getLogger();

        if (args.length < 1) {
            logger.fatal("Input file with events must be supplied to this program.");
            System.exit(1);
        }

        File inputFile = new File(args[0]);
        //File inputFile = new File("/home/deusemar/projetos/tw/GestaoTrilhaConferenciaSpock/input.txt");
        
        try {
            BufferedReader reader = new BufferedReader(new FileReader(inputFile));
            Conference conference = new ConferenceScheduler().schedule(reader);
            logger.info(conference);
        } catch (IOException e) {
            logger.fatal("Cannot read from input file: " + inputFile.getAbsolutePath());
            System.exit(1);
        }
    }
}
