package com.mbv.sched.jobs;

import com.mbv.framework.spring.ServiceContext;
import com.mbv.persist.dao.BankCodesDAO;
import com.mbv.persist.entity.BankCodes;
import com.mbv.persist.enums.Status;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by arindamnath on 21/03/16.
 */
public class BankCodeUpdaterJob extends QuartzJobBean {

    private static Logger logger = LoggerFactory
            .getLogger(BankCodeUpdaterJob.class.getCanonicalName());

    private BankCodesDAO bankCodesDAO = ServiceContext.getApplicationContext().getBean(BankCodesDAO.class);

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        readCSV("/Users/arindamnath/Desktop/bank_1.csv");
    }

    private void readCSV(String fileLocation) {
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ",";
        List<BankCodes> createList = new ArrayList<>();
        List<BankCodes> updateList = new ArrayList<>();
        try {

            br = new BufferedReader(new FileReader(fileLocation));
            while ((line = br.readLine()) != null) {
                String[] bank = line.split(cvsSplitBy);
                BankCodes bankCodes = bankCodesDAO.getBankCodeByIFSC(bank[1]);
                if(bankCodes == null) {
                    bankCodes = new BankCodes();
                    bankCodes.setBankName(bank[0]);
                    bankCodes.setIfscCode(bank[1]);
                    bankCodes.setBranch(bank[2]);
                    bankCodes.setAddress(bank[3]);
                    bankCodes.setCity(bank[4]);
                    bankCodes.setDistrict(bank[5]);
                    bankCodes.setState(bank[6]);
                    bankCodes.setStatus(Status.ACTIVE);
                    bankCodes.setCreatedBy(-1l);
                    bankCodes.setLastUpdatedBy(-1l);
                    createList.add(bankCodes);
                } else {
                    bankCodes.setBankName(bank[0]);
                    bankCodes.setBranch(bank[2]);
                    bankCodes.setAddress(bank[3]);
                    bankCodes.setCity(bank[4]);
                    bankCodes.setDistrict(bank[5]);
                    bankCodes.setState(bank[6]);
                    bankCodes.setStatus(Status.ACTIVE);
                    bankCodes.setCreatedBy(-1l);
                    bankCodes.setLastUpdatedBy(-1l);
                    updateList.add(bankCodes);
                }
                logger.debug("Bank [code= " + bank[1]
                        + " , name=" + bank[0] + "]");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        if(createList.size() > 0) {
            bankCodesDAO.bulkCreate(createList);
        }
        if(updateList.size() > 0) {
            bankCodesDAO.bulkUpdate(updateList);
        }
    }
}
