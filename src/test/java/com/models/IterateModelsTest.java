package com.models;

import static org.junit.Assert.*;

import java.time.LocalDate;
import java.time.Month;
import java.util.Set;

import org.junit.Test;

public class IterateModelsTest {
	
	@Test
	public void getExpiryListTest() {

//		for (int k : new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 }) {
//			int i = pStartDate + k;
//			Set<String> x = com.models.IterateModels.getEpiryList(i + "", 2, optionsHM); 
//			System.out.println(i + ": " + x);
//		}
	}

	@Test
	public void getMinsTo4pm_test() {
		assert (3*60.0) == com.models.utils.DateUtils.getMinsTo4pm("20220404130000") : 
			"Expecting 60*3 seconds to be diff between 20220404130000 and day closing";
		
		assert (0.0) == com.models.utils.DateUtils.getMinsTo4pm("20220404160000") : 
			"Expecting 0 mins at closing";
		
		assert (0.0) == com.models.utils.DateUtils.getMinsTo4pm("20220404170000") : 
			"Expecting 0 minutes after closing";
	}

	public void getMinsToExpireTest() {
		
	}

	@Test
	public void getWorkDayDiffTest() {
		assert 2 == com.models.utils.DateUtils.getWorkDayDiff( LocalDate.of(2022,Month.APRIL, 5),  LocalDate.of(2022,Month.APRIL, 6)) :
			"Expecting date diff == 2";
		assert 3 == com.models.utils.DateUtils.getWorkDayDiff( LocalDate.of(2022,Month.APRIL, 5),  LocalDate.of(2022,Month.APRIL, 7)) :
			"Expecting date diff == 3";
		assert 4 == com.models.utils.DateUtils.getWorkDayDiff( LocalDate.of(2022,Month.APRIL, 5),  LocalDate.of(2022,Month.APRIL, 8)) :
			"Expecting date diff == 4";
		assert 4 == com.models.utils.DateUtils.getWorkDayDiff( LocalDate.of(2022,Month.APRIL, 5),  LocalDate.of(2022,Month.APRIL, 9)) :
			"Expecting date diff == 4";
		assert 4 == com.models.utils.DateUtils.getWorkDayDiff( LocalDate.of(2022,Month.APRIL, 5),  LocalDate.of(2022,Month.APRIL, 10)) :
			"Expecting date diff == 4";
		assert 5 == com.models.utils.DateUtils.getWorkDayDiff( LocalDate.of(2022,Month.APRIL, 5),  LocalDate.of(2022,Month.APRIL, 11)) :
			"Expecting date diff == 5";
		assert 6 == com.models.utils.DateUtils.getWorkDayDiff( LocalDate.of(2022,Month.APRIL, 5),  LocalDate.of(2022,Month.APRIL, 12)) :
			"Expecting date diff == 6";
	}
	
	
	@Test
	public void getMinsToExpire() {
		assert 385 == com.models.utils.DateUtils.getMinsToExpire("20220405093500", "20220405") : " Expectign 385";
		assert 390+385 == com.models.utils.DateUtils.getMinsToExpire("20220405093500", "20220406") : " Expectign 775";
		// boundary conditions
		assert 0 == com.models.utils.DateUtils.getMinsToExpire("20220405173500", "20220405") : " Expectign 0";
		assert 390 == com.models.utils.DateUtils.getMinsToExpire("20220405173500", "20220406") : " Expectign 0";
	}
	
}
