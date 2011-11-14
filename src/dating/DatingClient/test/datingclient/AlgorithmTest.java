package datingclient;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.Before;

public class AlgorithmTest {
	public static final String[] testInput = {
		   "[0.6265, 0.2256, 0.6908, 0.2396, 0.8173, 0.5869, 0.7381, 0.6829, 0.895, 0.5633]:[-0.1896]",
		   "[0.4696, 0.5135, 0.9678, 0.562, 0.579, 0.0825, 0.2325, 0.5752, 0.6062, 0.3717]:[0.1193]",
		   "[0.132, 0.7885, 0.5777, 0.5645, 0.1575, 0.0629, 0.431, 0.1409, 0.8385, 0.1312]:[-0.0872]",
           "[0.4858, 0.5157, 0.3527, 0.4664, 0.1372, 0.9998, 0.3632, 0.4299, 0.6284, 0.6521]:[0.1548]",
           "[0.1425, 0.4873, 0.2362, 0.4128, 0.7792, 0.9916, 0.4478, 0.7617, 0.2798, 0.2781]:[-0.1176]",
           "[0.7449, 0.0262, 0.6757, 0.5868, 0.4208, 0.3821, 0.8251, 0.6373, 0.4521, 0.5341]:[-0.0252]",
           "[0.4578, 0.516, 0.8238, 0.0419, 0.1564, 0.9023, 0.8616, 0.2657, 0.386, 0.5646]:[-0.1206]",
           "[0.0658, 0.9455, 0.3916, 0.4817, 0.6596, 0.4995, 0.7664, 0.911, 0.5652, 0.2296]:[-0.4331]",
           "[0.7611, 0.2181, 0.8053, 0.7566, 0.9519, 0.3039, 0.3895, 0.1702, 0.7148, 0.0235]:[0.2139]",
           "[0.5895, 0.5789, 0.0632, 0.078, 0.1341, 0.9693, 0.798, 0.477, 0.2623, 0.5444]:[-0.2201]",
           "[0.5918, 0.8206, 0.7596, 7.0E-4, 0.5832, 0.3284, 0.0749, 0.5247, 0.2933, 0.7586]:[0.0837]",
           "[0.3631, 0.6424, 0.1906, 0.0975, 0.8347, 0.4382, 0.0323, 0.1814, 0.1035, 0.0762]:[0.0201]",
           "[0.1845, 0.2114, 0.3204, 0.0023, 0.4887, 0.4649, 0.7562, 0.6819, 0.8795, 0.3704]:[-0.4504]",
           "[0.1554, 0.2398, 0.8851, 0.5152, 0.6173, 0.9059, 0.3423, 0.085, 0.9869, 0.7425]:[0.2341]",
           "[0.1467, 0.696, 0.7316, 0.7333, 0.6237, 0.574, 0.0686, 0.9454, 0.1524, 0.5585]:[0.1932]",
           "[0.6667, 0.1415, 0.0071, 0.4667, 0.5937, 0.9174, 0.988, 0.9382, 0.3051, 0.1646]:[-0.3088]",
           "[0.3065, 0.9994, 0.3776, 0.4197, 0.3619, 0.2366, 0.3787, 0.4483, 0.5017, 0.6476]:[-0.0985]",
           "[0.736, 0.2384, 0.5926, 0.9522, 0.7158, 0.4228, 0.2443, 0.3864, 0.658, 0.0431]:[0.3065]",
           "[0.8295, 0.7799, 0.4256, 0.4735, 0.0195, 0.0197, 0.6199, 0.2771, 0.4155, 0.8432]:[-0.0031]",
           "[0.4923, 0.7387, 2.0E-4, 0.2691, 0.6386, 0.8315, 0.5527, 0.8755, 0.4995, 0.4118]:[-0.2664]"
	};
	
	
	
	Algorithm a;
	
	@Before 
	public void beforeTest(){
		 a = new Algorithm(10);
	}
	
	
	@Test
	public void testReadCandidates() {
		System.out.println("testign read");	
		String testInput = "[0.9626, 0.5476, 0.6633, 0.2155, 0.5082, 0.47, 0.9267, 0.155, 0.279, 0.9403]:[-0.0554]";
		a.readCandidates(testInput);	
		
	}

	@Test
	public void testGenerateCandidate() {
		fail("Not yet implemented");
	}
	
	@Test
	public void testGradientDes(){
		for(String input:  testInput){
			a.readCandidates(input);
		}
		assertTrue(validate(a.gradientDesTest()));
	} 
	
	@Test
	public void testpreProcess(){
		for(String input:  testInput){
			a.readCandidates(input);
		}
		assertTrue(validate(a.preProcessTest()));
	}
	
	@Test
	public void testDotProduct(){
		// OK
		double[] x= {1,2,3,4,5};
		double[] y= {1,2,3,4,5};
		assertEquals(55,a.dotProductTest(x,y),0.000001);
	}
	
	@Test
	public void testConvertFromDouble(){
		// OK
		Double[] x = {new Double(1), new Double(2), new Double(3)};
		double[] y = {1,2,3};
		assertArrayEquals(y, a.convertFromDoubleTest(x), 0.0001);
	}
	
	
	private boolean validate(double[] weight){
		double sum1 = 0;
		double sum2 = 0;
		for(int i = 0; i<weight.length; ++i){
			if(weight[i] > 0){
				sum1 += weight[i];
			}else{
				sum2 += weight[i];
			}
		}
		System.out.println(sum1);
		System.out.println(sum2);
		if(1-sum1 <0.000000001 && 1+sum2 < 0.000000001){
			return true;
		}
		return false;
	}

}
