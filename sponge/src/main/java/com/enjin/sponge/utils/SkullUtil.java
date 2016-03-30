package com.enjin.sponge.utils;

import com.enjin.core.Enjin;
import com.enjin.sponge.EnjinMinecraftPlugin;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.tileentity.TileEntity;
import org.spongepowered.api.data.manipulator.mutable.RepresentedPlayerData;
import org.spongepowered.api.data.manipulator.mutable.SkullData;
import org.spongepowered.api.profile.GameProfile;
import org.spongepowered.api.profile.GameProfileManager;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class SkullUtil {
	public static void updateSkullOwner(Location<World> location, String name) {
		Optional<TileEntity> optionalEntity = location.getTileEntity();
		if (optionalEntity.isPresent()) {
			updateSkullOwner(optionalEntity.get(), name);
		}
	}

	public static void updateSkullOwner(TileEntity entity, String name) {
		if (entity.supports(SkullData.class) && entity.supports(RepresentedPlayerData.class)) {
			EnjinMinecraftPlugin.getInstance().getAsync().execute(() -> {
				try {
					GameProfileManager manager = Sponge.getServer().getGameProfileManager();
					Optional<GameProfile> optionalProfile = manager.getCache().getByName(name);
					GameProfile profile = optionalProfile.isPresent() ? optionalProfile.get() : getProfile(name);

					if (profile != null) {
						if (!profile.getPropertyMap().containsKey("textures")) {
							CompletableFuture<GameProfile> future = Sponge.getServer().getGameProfileManager().fill(profile, true, false);
							future.whenComplete((p, e) -> {
								if (future.isCancelled())
									return;
								else if (future.isCompletedExceptionally()) {
									Enjin.getLogger().warning(e.getMessage());
									return;
								}

								updateGameProfile(p);
								updateSkullOwner(entity, p);
							});
						} else {
							updateSkullOwner(entity, profile);
						}
					}
				} catch (Exception e) {
					Enjin.getLogger().warning(e.getMessage());
				}
			});
		}
	}

	private static void updateSkullOwner(TileEntity entity, GameProfile profile) {
		EnjinMinecraftPlugin.getInstance().getSync().execute(() -> {
			RepresentedPlayerData data = entity.getOrCreate(RepresentedPlayerData.class).get();
			data.set(data.owner().set(profile));
			entity.offer(data);
		});
	}

	private static GameProfile getProfile(String name) throws Exception {
		UUID uuid = UUIDFetcher.getUUIDOf(name);
		GameProfile profile = null;

		if (uuid != null) {
			profile = Sponge.getServer().getGameProfileManager().createProfile(uuid, name);
		}

		return profile;
	}

	private static void updateGameProfile(GameProfile profile) {
		GameProfileManager manager = Sponge.getServer().getGameProfileManager();
		manager.getCache().add(profile, true, null);
	}
}