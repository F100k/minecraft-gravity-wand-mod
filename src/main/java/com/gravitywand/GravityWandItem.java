package com.gravitywand;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.function.Predicate;

public class GravityWandItem extends Item {
	public GravityWandItem(Properties properties) {
		super(properties);
	}

	@Override
	public InteractionResult use(Level level, Player player, InteractionHand hand) {
		ItemStack itemStack = player.getItemInHand(hand);

		if (!level.isClientSide()) {
			double reach = 30.0;
			Vec3 startPos = player.getEyePosition(1.0f);
			Vec3 lookVec = player.getViewVector(1.0f);
			Vec3 endPos = startPos.add(lookVec.scale(reach));

			// 1. Raycast for blocks first to limit search range
			ClipContext clipContext = new ClipContext(
				startPos,
				endPos,
				ClipContext.Block.COLLIDER,
				ClipContext.Fluid.NONE,
				player
			);
			BlockHitResult blockHitResult = level.clip(clipContext);

			double actualReach = reach;
			if (blockHitResult.getType() != HitResult.Type.MISS) {
				actualReach = startPos.distanceTo(blockHitResult.getLocation());
			}

			// 2. Raycast for entities up to the hit block position
			Vec3 entityEndPos = startPos.add(lookVec.scale(actualReach));
			AABB searchBox = player.getBoundingBox().expandTowards(lookVec.scale(actualReach)).inflate(1.0);
			Predicate<Entity> filter = entity -> (entity instanceof LivingEntity) && entity != player;

			EntityHitResult entityHitResult = ProjectileUtil.getEntityHitResult(
				player,
				startPos,
				entityEndPos,
				searchBox,
				filter,
				actualReach * actualReach
			);

			if (entityHitResult != null) {
				Entity target = entityHitResult.getEntity();
				
				// Launch entity into the air
				Vec3 launchVector = lookVec.scale(1.5).add(0.0, 1.2, 0.0);
				target.setDeltaMovement(launchVector);
				target.hurtMarked = true;

				level.playSound(null, player.getX(), player.getY(), player.getZ(),
					SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 1.0f, 1.0f);

				player.getCooldowns().addCooldown(itemStack, 20); // 1 second cooldown
				return InteractionResult.SUCCESS;
			} else if (blockHitResult.getType() == HitResult.Type.BLOCK) {
				BlockPos pos = blockHitResult.getBlockPos();
				BlockState state = level.getBlockState(pos);

				// Do not allow moving unbreakable blocks (like bedrock)
				if (!state.isAir() && state.getDestroySpeed(level, pos) >= 0) {
					level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);

					FallingBlockEntity fallingBlock = FallingBlockEntity.fall(
						level,
						pos,
						state
					);

					if (fallingBlock != null) {
						// Launch the block entity
						Vec3 blockLaunch = lookVec.scale(1.2).add(0.0, 1.0, 0.0);
						fallingBlock.setDeltaMovement(blockLaunch);
						fallingBlock.hurtMarked = true;

						level.playSound(null, player.getX(), player.getY(), player.getZ(),
							SoundEvents.FIREWORK_ROCKET_SHOOT, SoundSource.PLAYERS, 1.0f, 1.2f);

						player.getCooldowns().addCooldown(itemStack, 20); // 1 second cooldown
						return InteractionResult.SUCCESS;
					}
				}
			}
		}

		return level.isClientSide() ? InteractionResult.CONSUME : InteractionResult.SUCCESS;
	}
}
