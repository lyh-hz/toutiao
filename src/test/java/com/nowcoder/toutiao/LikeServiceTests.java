package com.nowcoder.toutiao;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

import com.nowcoder.toutiao.service.LikeService;


@RunWith(SpringRunner.class)
@SpringBootTest
public class LikeServiceTests {
	
	@Autowired
	LikeService likeService;
	
	@Test
	public void testLike() {
        likeService.like(123, 1, 1);
        Assert.assertEquals(1, likeService.getLikeStatus(123, 1, 1));
        
        likeService.disLike(123, 1, 1);
        Assert.assertEquals(-1, likeService.getLikeStatus(123, 1, 1));
	}
	
	@Test
	public void testB() {
		System.out.println("testB");

	}
	
    @Test(expected = IllegalArgumentException.class)
    public void testException() {
        throw new IllegalArgumentException("异常");
    }
	
	@Before
	public void setUp() {
		System.out.println("setUp");

	}
	
	@After
	public void tearDown() {
		System.out.println("tearDown");

	}
	
	@BeforeClass
	public static void beforeClass() {
		System.out.println("beforeClass");

	}
	
	@AfterClass
	public static void afterClass() {
		System.out.println("afterClass");
	}
}
