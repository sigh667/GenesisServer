package com.mokylin.bleach.test.gameserver.item;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.mokylin.bleach.common.core.GlobalData;
import com.mokylin.bleach.common.item.template.ItemTemplate;
import com.mokylin.bleach.core.template.TemplateService;
import com.mokylin.bleach.core.time.TimeService;
import com.mokylin.bleach.core.uuid.IUUIDGenerator;
import com.mokylin.bleach.gameserver.core.global.Globals;
import com.mokylin.bleach.gameserver.core.global.ServerGlobals;
import com.mokylin.bleach.gameserver.human.Human;
import com.mokylin.bleach.gameserver.item.Inventory;
import com.mokylin.bleach.test.dataserver.MockDataUpdater;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Globals.class)
public class InventoryTest {
	
	private static final ItemTemplate item1Template = new ItemTemplate();
	private static final int MAX_OVERLAP = 999;
	
	@BeforeClass
	public static void ready_for_test(){
		prepare_item_template_data();
	}
	
	private static void prepare_item_template_data() {
		item1Template.setId(1);
		item1Template.setMaxOverlap(MAX_OVERLAP);
	}
	
	private ServerGlobals sGlobals;
	private Human human;
	private IUUIDGenerator uuidGen;
	private Inventory inventory;
	
	@Before
	public void prepare_for_each_test_case(){
		prepare_mock_classes();
	}

	private void prepare_mock_classes() {
		//Prepare Globals
		PowerMockito.mockStatic(Globals.class);
		//Prepare Template Service
		TemplateService tService = mock(TemplateService.class);
		TimeService timeService = mock(TimeService.class);
		when(GlobalData.getTemplateService()).thenReturn(tService);
		when(Globals.getTimeService()).thenReturn(timeService);
		
		when(tService.get(1, ItemTemplate.class)).thenReturn(item1Template);
		when(timeService.now()).thenReturn(1l);
		
		sGlobals = mock(ServerGlobals.class);
		human = mock(Human.class);
		uuidGen = mock(IUUIDGenerator.class);
		when(human.getServerGlobals()).thenReturn(sGlobals);
		when(human.getDataUpdater()).thenReturn(new MockDataUpdater());
		when(sGlobals.getUUIDGenerator()).thenReturn(uuidGen);
		inventory = new Inventory(human);
	}

	@Test
	public void addItem_should_add_item_in_the_inventory_successful() {
		final int item1Amount = 10;
		//before adding
		assertThat(inventory.containsItem(item1Template.getId()), is(false));
		//adding
		assertThat(inventory.addItem(item1Template.getId(), item1Amount), is(true));
		//after adding
		assertThat(inventory.containsItem(item1Template.getId()), is(true));
		assertThat(inventory.getItemAmount(item1Template.getId()), is(item1Amount));
		
		//add again
		assertThat(inventory.addItem(item1Template.getId(), item1Amount), is(true));
		assertThat(inventory.containsItem(item1Template.getId()), is(true));
		assertThat(inventory.getItemAmount(item1Template.getId()), is(item1Amount * 2));
	}
	
	@Test
	public void addItem_should_not_add_more_than_max_overlap_amount(){
		final int item1Amount = 10;
		assertThat(inventory.addItem(item1Template.getId(), item1Amount), is(true));
		assertThat(inventory.containsItem(item1Template.getId()), is(true));
		assertThat(inventory.getItemAmount(item1Template.getId()), is(item1Amount));
		
		assertThat(inventory.addItem(item1Template.getId(), MAX_OVERLAP), is(true));
		assertThat(inventory.containsItem(item1Template.getId()), is(true));
		assertThat(inventory.getItemAmount(item1Template.getId()), is(MAX_OVERLAP));
	}
	
	@Test
	public void deleteItem_should_delete_item_from_inventory_successful(){
		final int item1Amount = 90;
		inventory.addItem(item1Template.getId(), item1Amount);
		assertThat(inventory.deleteItem(item1Template.getId(), 77), is(true));
		assertThat(inventory.getItemAmount(item1Template.getId()), is(90 - 77));
	}
	
	@Test
	public void deleteItem_should_failed_if_delete_amount_is_greater_than_item_overlap(){
		final int item1Amount = 12;
		inventory.addItem(item1Template.getId(), item1Amount);
		assertThat(inventory.deleteItem(item1Template.getId(), 15), is(false));
		assertThat(inventory.getItemAmount(item1Template.getId()), is(item1Amount));
	}
	
	@Test
	public void deleteItem_should_remove_item_from_inventory_when_item_overlap_is_zero(){
		final int item1Amount = 12;
		inventory.addItem(item1Template.getId(), item1Amount);
		//before deleting
		assertThat(inventory.containsItem(item1Template.getId()), is(true));
		//deleting
		assertThat(inventory.deleteItem(item1Template.getId(), item1Amount), is(true));
		//after deleting
		assertThat(inventory.getItemAmount(item1Template.getId()), is(0));
		assertThat(inventory.containsItem(item1Template.getId()), is(false));
	}

}
