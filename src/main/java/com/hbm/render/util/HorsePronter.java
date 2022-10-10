package com.hbm.render.util;

import org.lwjgl.opengl.GL11;

import com.hbm.lib.RefStrings;
import com.hbm.render.loader.HFRWavefrontObject;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.model.IModelCustom;

public class HorsePronter {
	
	public static final IModelCustom horse = new HFRWavefrontObject(new ResourceLocation(RefStrings.MODID, "models/mobs/horse.obj"), false).asDisplayList();

	public static final ResourceLocation tex_demohorse = new ResourceLocation(RefStrings.MODID, "textures/models/horse/horse_template.png");

	private Vec3[] pose = new Vec3[] {
			Vec3.createVectorHelper(0, 0, 0), //head
			Vec3.createVectorHelper(0, 0, 0), //left front leg
			Vec3.createVectorHelper(0, 0, 0), //right front leg
			Vec3.createVectorHelper(0, 0, 0), //left back leg
			Vec3.createVectorHelper(0, 0, 0), //left back leg
			Vec3.createVectorHelper(0, 0, 0), //tail
			Vec3.createVectorHelper(0, 0, 0), //body
			Vec3.createVectorHelper(0, 0, 0) //body offset
	};

	private Vec3[] offsets = new Vec3[] {
			Vec3.createVectorHelper(0, 0, 0), //head
			Vec3.createVectorHelper(0, 0, 0), //left front leg
			Vec3.createVectorHelper(0, 0, 0), //right front leg
			Vec3.createVectorHelper(0, 0, 0), //left back leg
			Vec3.createVectorHelper(0, 0, 0), //left back leg
			Vec3.createVectorHelper(0, 0, 0), //tail
			Vec3.createVectorHelper(0, 0, 0), //body
			Vec3.createVectorHelper(0, 0, 0) //body offset
	};

	public static final int id_head = 0;
	public static final int id_lfl = 1;
	public static final int id_rfl = 2;
	public static final int id_lbl = 3;
	public static final int id_rbl = 4;
	public static final int id_tail = 5;
	public static final int id_body = 6;
	public static final int id_position = 7;

	private boolean wings = false;
	private boolean horn = false;
	private boolean maleSnoot = false;

	public void reset() {
		
		wings = false;
		horn = false;
		
		for(Vec3 angles : pose) {
			angles.xCoord = 0;
			angles.yCoord = 0;
			angles.zCoord = 0;
		}
	}
	
	public void enableHorn() { horn = true; }
	public void enableWings() { wings = true; }
	public void setMaleSnoot() { maleSnoot = true; }
	
	public void setAlicorn() {
		enableHorn();
		enableWings();
	}
	
	public void pose(int id, double yaw, double pitch, double roll) {
		pose[id].xCoord = yaw;
		pose[id].yCoord = pitch;
		pose[id].zCoord = roll;
	}
	
	public void pront() {
		
		GL11.glPushMatrix();
		doTransforms(id_body);
		
		horse.renderPart("Body");
		
		if(horn) {
			renderWithTransform(id_head, "Head", maleSnoot ? "NoseMale" : "NoseFemale", "HornPointy");
		} else {
			renderWithTransform(id_head, "Head", maleSnoot ? "NoseMale" : "NoseFemale");
		}
		
		renderWithTransform(id_lfl, "LeftFrontLeg");
		renderWithTransform(id_rfl, "RightFrontLeg");
		renderWithTransform(id_lbl, "LeftBackLeg");
		renderWithTransform(id_rbl, "RightBackLeg");
		renderWithTransform(id_tail, "Tail");
		
		if(wings) {
			horse.renderPart("LeftWing");
			horse.renderPart("RightWing");
		}
		
		GL11.glPopMatrix();
	}
	
	private void doTransforms(int id) {
		Vec3 rotation = pose[id];
		Vec3 offset = offsets[id];
		GL11.glTranslated(offset.xCoord, offset.yCoord, offset.zCoord);
		GL11.glRotated(rotation.xCoord, 0, 1, 0);
		GL11.glRotated(rotation.yCoord, 1, 0, 0);
		GL11.glRotated(rotation.zCoord, 0, 0, 1); //TODO: check pitch and roll axis
		GL11.glTranslated(-offset.xCoord, -offset.yCoord, -offset.zCoord);
	}
	
	private void renderWithTransform(int id, String... parts) {
		GL11.glPushMatrix();
		doTransforms(id);
		for(String part : parts) horse.renderPart(part);
		GL11.glPopMatrix();
	}
}