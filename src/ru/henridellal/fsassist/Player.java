package ru.henridellal.fsassist;

import android.content.Context;
import android.graphics.Color;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Player
{
	private static final char[] LIM_CHARS = new char[]{'E','D','C','B','A'};
	/* ATTR_LIM is a list of maximum
	   skill values for each char
	*/
	private static final int[] ATTR_LIM = new int[]{0, 4, 8, 12, 16,20};
	/* Skill importance from 0 to 5
	   for every position
	 */
	private static final int[] GK = new int[]{5, 0, 4, 0, 3, 2, 2, 0, 0, 0,4, 3, 1, 2, 0};
	private static final int[] SW = new int[]{0, 4, 4, 3, 4, 1, 1, 1, 1, 1, 3, 4, 3, 3, 1};
	private static final int[] DLR = new int[]{0,4,4,3,4, 1,2,2,2,1, 3,2,3,3,2};
	private static final int[] WBLR = new int[]{0,4, 4, 3, 4, 1, 2, 3, 3, 1, 3, 2, 5, 4, 2};
	private static final int[] DC = new int[]{0, 5, 4, 5, 5, 1, 1, 2, 1, 1,2,4,3,3,1};
	private static final int[] DMC = new int[]{0, 5,4,3,5,2,3,3,1,1,2,3,4,3,1};
	private static final int[] MLR = new int[]{0, 1,3,1,1,3,4,4,4,3,4,2,4,4,3};
	private static final int[] AMLR = new int[]{0, 1,3,1,1,3,4,5,5,3,4,2,4,4,4};
	private static final int[] AMC = new int[]{0, 1,3,1,1,5,5,4,2,4,3,3,3,3,5};
	private static final int[] MC = new int[]{0, 1,3,1,1,5,5,4,2,3,3,3,3,3,5};
	private static final int[] ST = new int[]{0, 1, 3,4,1,3,3,4,2,5,4,4,4,4,2};
	private static final int[][] SKILL_IMPORTANCE = new int[][]{GK, SW, DLR, WBLR, DC, DMC, MLR, AMLR, AMC, MC, ST};
	//all positions available
	private static final String[] POSITIONS = {"GK", "SW", "DLR", "WBLR", "DC", "DMC", "MLR", "AMLR", "AMC", "MC", "ST"};
	
	private static final int[] MAXRATE = new int[]{26,34,36,41,38,40,41,44,43,42,44};
	
	private float skillLevel;
	
	//rates for each position in %
	private ArrayList<Float> posRates;
	
	//list of 15 player attributes
	private int[] attr;
	
	/* chars used for colouring
			skill values		*/
	private char[] attrChars;
	
	private String name, surname, fullName;
	private long value;
	private int age, cond, pot, wage;
	private float rating;
	
	public int compareInts(int first, int second) {
		if (first > second) {
			return -1;
		} else if (first < second) {
			return 1;
		} else {
			return 0;
		}
	}
	public int compareFloats(float first, float second) {
		if (first > second) {
			return -1;
		} else if (first < second) {
			return 1;
		} else {
			return 0;
		}
	}
	public int compareLongs(long first, long second) {
		if (first > second) {
			return -1;
		} else if (first < second) {
			return 1;
		} else {
			return 0;
		}
	}
	public int compareInfo(Player pl, int mode) {
		switch(mode) {
			case Team.MODE_NAME:
				return this.fullName.compareTo(pl.getFullName());
			case Team.MODE_AGE:
				return compareInts(this.age, pl.getAge());
			case Team.MODE_POTENTIAL:
				return compareInts(this.pot, pl.getPotential());
			case Team.MODE_CONDITION:
				return compareInts(this.cond, pl.getCondition());
			case Team.MODE_WAGE:
				return compareInts(this.wage, pl.getWage());
			case Team.MODE_VALUE:
				return compareLongs(this.value, pl.getValue());
			case Team.MODE_RATING:
				return compareFloats(this.rating, pl.getRating());
			case Team.MODE_SKILL_LEVEL:
				return compareFloats(this.skillLevel, pl.getSkillLevel());
			default:
				return 0;
		}
	}
	public int compareAttr(Player pl, int mode) {
		if (this.attr[mode] > pl.getAttr(mode)) {
			return -1;
		} else if (this.attr[mode] < pl.getAttr(mode)) {
			return 1;
		} else {
			return 0;
		}
	}
	public int compareRate(Player pl, int mode) {
		if (this.posRates.get(mode) > pl.getRate(mode)) {
			return -1;
		} else if (this.posRates.get(mode) < pl.getRate(mode)) {
			return 1;
		} else {
			return 0;
		}
	}
	public float getSkillLevel(){
		return this.skillLevel;
	}
	private void setSkillLevel(){
		this.skillLevel = 0.0f;
		for (int i: this.attr) {
			this.skillLevel += (float)i;
		}
		this.skillLevel = shortenFloat(this.skillLevel/15);
	}
	public int getAge() {
		return this.age;
	}
	public int getWage() {
		return this.wage;
	}
	public int getPotential() {
		return this.pot;
	}
	public int getCondition() {
		return this.cond;
	}
	public long getValue() {
		return this.value;
	}
	public float getRating() {
		return this.rating;
	}
	public int getAttr(int i) {
		return this.attr[i];
	}
	public float getRate(int i) {
		return this.posRates.get(i);
	}
	public String getName() {
		return this.name;
	}
	public String getSurname() {
		return this.surname;
	}
	public String getFullName() {
		return this.fullName;
	}
	private void setRates() {
		int[] absRate = new int[15];
		this.posRates = new ArrayList<Float>();
		
		for (int i = 0; i < 11; i++) {
			for (int j = 0; j < 15; j++) {
				absRate[j] = this.attr[j] * SKILL_IMPORTANCE[i][j];
			}
			this.posRates.add(.0f);
			for (int k = 0; k < 15; k++) {
				this.posRates.set(i, posRates.get(i)+absRate[k]);
			}
			this.posRates.set(i, posRates.get(i)/MAXRATE[i]);
			this.posRates.set(i, shortenFloat(this.posRates.get(i)*5));
		}
	}
	public ArrayList<Float> getRates() {
		return this.posRates;
	}
	public String getBestRates(int ratesAmount){
		//float[] ar = new float[this.posRate.length];
		int j;
		String bestRates = new String();
		ArrayList<Float> list = new ArrayList<Float>(this.posRates);
		//System.arraycopy(this.posRates, 0, list, 0, this.posRates.size());
		for (int i = 0; i < ratesAmount; i++){
			j = list.indexOf(Collections.max(list));
			bestRates = bestRates.concat(POSITIONS[j]).concat("(").concat(Float.toString(list.get(j))).concat(") ");
			list.set(j, 0.f);
		}
		return bestRates;
	}
	public int getAttrColor(Context c, int attrIndex){
		switch (this.attrChars[attrIndex]) {
			case 'E':
				return c.getResources().getColor(R.color.attr_e);
			case 'D':
				return c.getResources().getColor(R.color.attr_d);
			case 'C':
				return c.getResources().getColor(R.color.attr_c);
			case 'B':
				return c.getResources().getColor(R.color.attr_b);
			default:
				return c.getResources().getColor(R.color.attr_a);
		}
	}
	private void setAttrChars(){
		//arr_char = []
		this.attrChars = new char[15];
		for (int i = 0; i < 15; i++){
			for (int j = 1; j < 6; j++){
				if (this.attr[i] <= ATTR_LIM[j] && this.attr[i] > ATTR_LIM[j-1]) {
					this.attrChars[i] = LIM_CHARS[j-1];
				}
			}
		}
	}
	public void fillAttrArray(String[] strAttributes) {
		for (int i = 0; i<strAttributes.length; i++) {
			attr[i] = Integer.parseInt(strAttributes[i]);
		}
	}
	public float shortenFloat(float f) {
		f = f*1000;
		int tmp = (int) f;
		tmp = tmp % 10 > 5 ? tmp + 10 : tmp;
		tmp /= 10;
		float res = ((float) tmp)/100;
		return res;
	}
	public long financeToNumber(String finance) {
		StringBuilder tmp = new StringBuilder(finance);
		tmp = tmp.delete(tmp.length()-3, tmp.length());
		int commaIndex;
		while ((commaIndex = tmp.indexOf(",")) != -1) {
			tmp.deleteCharAt(commaIndex);
		}
		return Long.parseLong(tmp.toString());
	}
	public Player(String[] data) {
		this.name = data[0];
		this.surname = data[1];
		//this.fullName = this.name.charAt(0) + ".".concat(this.surname);
		this.fullName = this.name + " ".concat(this.surname);
		this.age = Integer.valueOf(data[2]);
		this.value = financeToNumber(data[3]);
		//this.value.replaceAll(",", "");
		this.wage = (int)financeToNumber(data[4]);
		this.cond = data[5].equals("Injured") ? 0 : Integer.valueOf(data[5]);
		this.rating = Float.valueOf(data[7]);
		this.attr = new int[15];
		fillAttrArray(Arrays.copyOfRange(data, 8, data.length));
		this.setAttrChars();
		this.pot = Integer.valueOf(data[6]);
		this.setRates();
		//sortRates();
		//this.setBestRates(3);
		this.setSkillLevel();
		//this.info = {this.age, this.cond, this.pot, this.rating, this.skillLevel};
	}
	public ArrayList<String> getData(Context context){
		ArrayList<String> data = new ArrayList<String>();
		data.add(fullName);
		data.add(((Integer)age).toString());
		data.add("$" + ((Long)value).toString());
		data.add("$" + ((Integer)wage).toString());
		data.add(cond > 0 ? ((Integer)cond).toString()+"%" : context.getResources().getString(R.string.injured));
		data.add(((Integer)pot).toString());
		data.add(((Float)rating).toString());
		data.add(((Float)skillLevel).toString());
		for (int i=0; i<attr.length; i++) {
			data.add(((Integer)attr[i]).toString());
		}
		return data;
	}
	public ArrayList<String> getDetailedData(Context context){
		ArrayList<String> detailedData = getData(context);
		for (Float each: getRates())
			detailedData.add(each.toString());
		return detailedData;
	}
}
