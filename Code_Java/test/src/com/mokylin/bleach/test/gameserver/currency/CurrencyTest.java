package com.mokylin.bleach.test.gameserver.currency;

import static org.junit.Assert.assertArrayEquals;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.mokylin.bleach.common.currency.Currency;
import com.mokylin.bleach.common.currency.CurrencyPropId;
import com.mokylin.bleach.gamedb.orm.entity.HumanEntity;
import com.mokylin.bleach.gameserver.human.Human;
import com.mokylin.bleach.test.gameserver.battleprop.MockHuman;

public class CurrencyTest {

	@Test
	public void the_currency_should_behave_correctly() {
		HumanEntity entity = new HumanEntity();
		entity.setChargeDiamond(101);
		entity.setFreeDiamond(100);
		entity.setGold(100);
		
		Human human = new MockHuman();
		human.fromEntity(entity);
		
//		assertThat(human.get(CurrencyPropId.CHARGE_DIAMOND), is(101l));
//		assertThat(human.get(CurrencyPropId.FREE_DIAMOND), is(100l));
//		assertThat(human.get(CurrencyPropId.GOLD), is(100l));
		
		human.giveMoney(CurrencyPropId.CHARGE_DIAMOND, 200);
		human.giveMoney(CurrencyPropId.GOLD, 300);
		
//		assertThat(human.get(CurrencyPropId.CHARGE_DIAMOND), is(301l));
//		assertThat(human.get(CurrencyPropId.FREE_DIAMOND), is(100l));
//		assertThat(human.get(CurrencyPropId.GOLD), is(400l));
		assertArrayEquals(new Boolean[] { human.isMoneyEnough(Currency.DIAMOND, 500) }, new Boolean[] { false });
		assertArrayEquals(new Boolean[] { human.isMoneyEnough(Currency.DIAMOND, 350) }, new Boolean[] { true });
		assertArrayEquals(new Boolean[] { human.isMoneyEnough(Currency.GOLD, 500) }, new Boolean[] { false });
		assertArrayEquals(new Boolean[] { human.isMoneyEnough(Currency.GOLD, 400) }, new Boolean[] { true });
		
		human.costMoney(Currency.DIAMOND, 350);
		human.costMoney(Currency.GOLD, 350);
		
//		assertThat(human.get(CurrencyPropId.CHARGE_DIAMOND), is(0l));
//		assertThat(human.get(CurrencyPropId.FREE_DIAMOND), is(51l));
//		assertThat(human.get(CurrencyPropId.GOLD), is(50l));
	}
	
	@Rule
	public ExpectedException thrown = ExpectedException.none();
	
	@Test
	public void use_isMoneyEnough_with_charge_diamond_will_throw_exception() {
		thrown.expect(IllegalArgumentException.class);
		
		HumanEntity entity = new HumanEntity();
		entity.setChargeDiamond(101);
		entity.setFreeDiamond(100);
		entity.setGold(100);
		
		Human human = new MockHuman();
		human.fromEntity(entity);
		
//		human.isMoneyEnough(CurrencyPropId.CHARGE_DIAMOND, 50);
	}
	
	@Test
	public void use_isMoneyEnough_with_free_diamond_will_throw_exception() {
		thrown.expect(IllegalArgumentException.class);
		
		HumanEntity entity = new HumanEntity();
		entity.setChargeDiamond(101);
		entity.setFreeDiamond(100);
		entity.setGold(100);
		
		Human human = new MockHuman();
		human.fromEntity(entity);
		
//		human.isMoneyEnough(CurrencyPropId.FREE_DIAMOND, 50);
	}
	
	@Test
	public void use_costMoney_with_charge_diamond_will_throw_exception() {
		thrown.expect(IllegalArgumentException.class);
		
		HumanEntity entity = new HumanEntity();
		entity.setChargeDiamond(101);
		entity.setFreeDiamond(100);
		entity.setGold(100);
		
		Human human = new MockHuman();
		human.fromEntity(entity);
		
//		human.costMoney(CurrencyPropId.CHARGE_DIAMOND, 30);
	}
	
	@Test
	public void use_costMoney_with_free_diamond_will_throw_exception() {
		thrown.expect(IllegalArgumentException.class);
		
		HumanEntity entity = new HumanEntity();
		entity.setChargeDiamond(101);
		entity.setFreeDiamond(100);
		entity.setGold(100);
		
		Human human = new MockHuman();
		human.fromEntity(entity);
		
//		human.costMoney(CurrencyPropId.FREE_DIAMOND, 30);
	}
	
	@Test
	public void use_isMoneyEnough_with_zero_value_will_throw_exception() {
		thrown.expect(IllegalArgumentException.class);
		
		HumanEntity entity = new HumanEntity();
		entity.setChargeDiamond(101);
		entity.setFreeDiamond(100);
		entity.setGold(100);
		
		Human human = new MockHuman();
		human.fromEntity(entity);
		
		human.isMoneyEnough(Currency.GOLD, 0);
	}
	
	@Test
	public void use_isDiamondEnough_with_zero_value_will_throw_exception() {
		thrown.expect(IllegalArgumentException.class);
		
		HumanEntity entity = new HumanEntity();
		entity.setChargeDiamond(101);
		entity.setFreeDiamond(100);
		entity.setGold(100);
		
		Human human = new MockHuman();
		human.fromEntity(entity);
		
		human.isMoneyEnough(Currency.DIAMOND, 0);
	}
	
	@Test
	public void use_costMoney_with_zero_value_will_throw_exception() {
		thrown.expect(IllegalArgumentException.class);
		
		HumanEntity entity = new HumanEntity();
		entity.setChargeDiamond(101);
		entity.setFreeDiamond(100);
		entity.setGold(100);
		
		Human human = new MockHuman();
		human.fromEntity(entity);
		
		human.costMoney(Currency.GOLD, 0);
	}
	
	@Test
	public void use_costDiamond_with_zero_value_will_throw_exception() {
		thrown.expect(IllegalArgumentException.class);
		
		HumanEntity entity = new HumanEntity();
		entity.setChargeDiamond(101);
		entity.setFreeDiamond(100);
		entity.setGold(100);
		
		Human human = new MockHuman();
		human.fromEntity(entity);
		
		human.costMoney(Currency.DIAMOND, 0);
	}
	
	@Test
	public void use_giveMoney_with_zero_value_will_throw_exception() {
		thrown.expect(IllegalArgumentException.class);
		
		HumanEntity entity = new HumanEntity();
		entity.setChargeDiamond(101);
		entity.setFreeDiamond(100);
		entity.setGold(100);
		
		Human human = new MockHuman();
		human.fromEntity(entity);
		
		human.giveMoney(CurrencyPropId.GOLD, 0);
	}
	
	@Test
	public void use_isMoneyEnough_with_negative_value_will_throw_exception() {
		thrown.expect(IllegalArgumentException.class);
		
		HumanEntity entity = new HumanEntity();
		entity.setChargeDiamond(101);
		entity.setFreeDiamond(100);
		entity.setGold(100);
		
		Human human = new MockHuman();
		human.fromEntity(entity);
		
		human.isMoneyEnough(Currency.GOLD, -1);
	}
	
	@Test
	public void use_isDiamondEnough_with_negative_value_will_throw_exception() {
		thrown.expect(IllegalArgumentException.class);
		
		HumanEntity entity = new HumanEntity();
		entity.setChargeDiamond(101);
		entity.setFreeDiamond(100);
		entity.setGold(100);
		
		Human human = new MockHuman();
		human.fromEntity(entity);
		
		human.isMoneyEnough(Currency.DIAMOND, -1);
	}
	
	@Test
	public void use_costMoney_with_negative_value_will_throw_exception() {
		thrown.expect(IllegalArgumentException.class);
		
		HumanEntity entity = new HumanEntity();
		entity.setChargeDiamond(101);
		entity.setFreeDiamond(100);
		entity.setGold(100);
		
		Human human = new MockHuman();
		human.fromEntity(entity);
		
		human.costMoney(Currency.GOLD, -1);
	}
	
	@Test
	public void use_costDiamond_with_negative_value_will_throw_exception() {
		thrown.expect(IllegalArgumentException.class);
		
		HumanEntity entity = new HumanEntity();
		entity.setChargeDiamond(101);
		entity.setFreeDiamond(100);
		entity.setGold(100);
		
		Human human = new MockHuman();
		human.fromEntity(entity);
		
		human.costMoney(Currency.DIAMOND, -1);
	}
	
	@Test
	public void use_giveMoney_with_negative_value_will_throw_exception() {
		thrown.expect(IllegalArgumentException.class);
		
		HumanEntity entity = new HumanEntity();
		entity.setChargeDiamond(101);
		entity.setFreeDiamond(100);
		entity.setGold(100);
		
		Human human = new MockHuman();
		human.fromEntity(entity);
		
		human.giveMoney(CurrencyPropId.GOLD, -1);
	}
}
