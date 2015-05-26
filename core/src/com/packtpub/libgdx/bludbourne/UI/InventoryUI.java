package com.packtpub.libgdx.bludbourne.UI;

import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.badlogic.gdx.utils.Array;
import com.packtpub.libgdx.bludbourne.InventoryItemFactory;
import com.packtpub.libgdx.bludbourne.InventoryItem.ItemUseType;
import com.packtpub.libgdx.bludbourne.InventoryItem.ItemTypeID;

public class InventoryUI extends Window {

    private int _numSlots = 50;
    private int _lengthSlotRow = 10;
    private Table _inventorySlotTable;
    private Table _playerSlotsTable;
    private Table _equipSlots;
    private DragAndDrop _dragAndDrop;
    private Array<Actor> _inventoryActors;

    private final int _slotWidth = 52;
    private final int _slotHeight = 52;

    public InventoryUI(final Skin skin, TextureAtlas textureAtlas){
        super("Inventory", skin, "solidbackground");

        _dragAndDrop = new DragAndDrop();
        _inventoryActors = new Array<Actor>();

        //create
        _inventorySlotTable = new Table();
        _playerSlotsTable = new Table();
        _equipSlots = new Table();
        _equipSlots.defaults().space(10);

        InventorySlot headSlot = new InventorySlot(
                ItemUseType.ARMOR_HELMET.getValue(),
                new Image(PlayerHUD.itemsTextureAtlas.findRegion("inv_helmet")));
        _inventoryActors.add(headSlot.getInventorySlotTooltip());

        InventorySlot leftArmSlot = new InventorySlot(
                ItemUseType.WEAPON_ONEHAND.getValue() |
                ItemUseType.WEAPON_TWOHAND.getValue() |
                ItemUseType.ARMOR_SHIELD.getValue() |
                ItemUseType.WAND_ONEHAND.getValue() |
                ItemUseType.WAND_TWOHAND.getValue(),
                new Image(PlayerHUD.itemsTextureAtlas.findRegion("inv_weapon"))
        );
        _inventoryActors.add(leftArmSlot.getInventorySlotTooltip());

        InventorySlot rightArmSlot = new InventorySlot(
                ItemUseType.WEAPON_ONEHAND.getValue() |
                ItemUseType.WEAPON_TWOHAND.getValue() |
                ItemUseType.ARMOR_SHIELD.getValue() |
                ItemUseType.WAND_ONEHAND.getValue() |
                ItemUseType.WAND_TWOHAND.getValue(),
                new Image(PlayerHUD.itemsTextureAtlas.findRegion("inv_shield"))
        );
        _inventoryActors.add(rightArmSlot.getInventorySlotTooltip());

        InventorySlot chestSlot = new InventorySlot(
                ItemUseType.ARMOR_CHEST.getValue(),
                new Image(PlayerHUD.itemsTextureAtlas.findRegion("inv_chest")));
        _inventoryActors.add(chestSlot.getInventorySlotTooltip());

        InventorySlot legsSlot = new InventorySlot(
                ItemUseType.ARMOR_FEET.getValue(),
                new Image(PlayerHUD.itemsTextureAtlas.findRegion("inv_boot")));
        _inventoryActors.add(legsSlot.getInventorySlotTooltip());

        _dragAndDrop.addTarget(new InventorySlotTarget(headSlot));
        _dragAndDrop.addTarget(new InventorySlotTarget(leftArmSlot));
        _dragAndDrop.addTarget(new InventorySlotTarget(chestSlot));
        _dragAndDrop.addTarget(new InventorySlotTarget(rightArmSlot));
        _dragAndDrop.addTarget(new InventorySlotTarget(legsSlot));

        _playerSlotsTable.setBackground(new Image(new NinePatch(textureAtlas.createPatch("dialog"))).getDrawable());

        //layout
        for(int i = 1; i <= _numSlots; i++){
            InventorySlot inventorySlot = new InventorySlot();
            _inventoryActors.add(inventorySlot.getInventorySlotTooltip());
            _dragAndDrop.addTarget(new InventorySlotTarget(inventorySlot));

            _inventorySlotTable.add(inventorySlot).size(_slotWidth, _slotHeight);

            if(i % _lengthSlotRow == 0){
                _inventorySlotTable.row();
            }
        }

        _equipSlots.add();
        _equipSlots.add(headSlot).size(_slotWidth, _slotHeight);
        _equipSlots.row();

        _equipSlots.add(leftArmSlot).size(_slotWidth, _slotHeight);
        _equipSlots.add(chestSlot).size(_slotWidth, _slotHeight);
        _equipSlots.add(rightArmSlot).size(_slotWidth, _slotHeight);
        _equipSlots.row();

        _equipSlots.add();
        _equipSlots.right().add(legsSlot).size(_slotWidth, _slotHeight);

        _playerSlotsTable.add(_equipSlots);

        this.add(_playerSlotsTable).padBottom(20).row();
        this.add(_inventorySlotTable).row();
        this.pack();
    }

    public void populateInventory(Array<ItemTypeID> itemTypeIDs){
        Array<Cell> cells = _inventorySlotTable.getCells();
        for(int i = 0; i < itemTypeIDs.size; i++){
            InventorySlot inventorySlot =  ((InventorySlot)cells.get(i).getActor());
            inventorySlot.add(InventoryItemFactory.getInstance().getInventoryItem(itemTypeIDs.get(i)));
            _dragAndDrop.addSource(new InventorySlotSource(inventorySlot, _dragAndDrop));
        }
    }

    public Array<Actor> getInventoryActors(){
        return _inventoryActors;
    }
}
