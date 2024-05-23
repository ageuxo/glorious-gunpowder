package io.github.ageuxo.gloriousgunpowder.client.render;

import io.github.ageuxo.gloriousgunpowder.data.stats.GunStats;
import io.github.ageuxo.gloriousgunpowder.event.GunFiredEvent;
import io.github.ageuxo.gloriousgunpowder.item.AbstractFirearm;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;

public class GunRenderHandler {
    private float recoil;
    private float recoilProgress;
    @SubscribeEvent
    public void onRenderTick(ClientTickEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();
        if(this.recoil <= 0 || mc.player == null)
            return;

        float recoilAmount = this.recoil * mc.getDeltaFrameTime() * 5f;
        float startProgress = this.recoilProgress / this.recoil;
        float endProgress = (this.recoilProgress + recoilAmount) / this.recoil;

        float xRot = mc.player.getXRot();
        if(startProgress < 0.2f) {
            mc.player.setXRot(xRot - ((endProgress - startProgress) / 0.1f) * this.recoil);
        }

        this.recoilProgress += recoilAmount;

        if(recoilProgress >= recoil) {
            this.recoil = 0;
            this.recoilProgress = 0;
        }
    }
    @SubscribeEvent
    public void onGunFire(GunFiredEvent event) {
        if(event.getShooter() instanceof AbstractClientPlayer && event.getGun().getItem() instanceof AbstractFirearm firearm) {
            this.recoil = firearm.getGunAttributeValue(event.getGun(), GunStats.RECOIL.get());
        }
    }

}
