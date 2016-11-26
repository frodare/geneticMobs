# DNA Description / Notes

##Environmental Factors
- Heath `0-256`
- Target Distance `0-256`
- Has Target `0-1`
- Enemies Nearby `0-1`
- Can Attack `0-1`
- Is Moving `0-1`

##Stimuli
- X non stimulus tick
- D `onTakeDamage`
- S `seeEntity`
- B `isBurning`
- A `attack`
- Before change?

##Actions
- j Jump/Swim `getJumpHelper().setJumping()`
- p Panic
- r Run (away from target)
- a Attack
- c Charge
- w Wander

##Gene Byte Format (single java long)
Gene Type
- `byte 0` Stimulus Code
- `byte 1` Action Code

Gene Behavior Values
- `byte 2` Distance from target
- `byte 3` Has target

- `byte 4` Nearby enemies
- `byte 5` Can attack

- `byte 6` Is moving, has a path
- `byte 7` Health

##Behavior Algorithm
- find all genes for the given stimulus
- check all environmental factors aginst gene values
	+ if gene factor is greater store behavior with a score of the difference
	+ pick lowest scoring gene (the one that closest matches the environmental factors)
	+ invoke action of select gene

##Breeding Algorithm

###By Concatenation and Removal
- Concatenate both gene sequences 
- Choose a random number of genes to remove between 1/4 to 3/4

###Gene Averaging
- Keep all common genes (same first two byte) and averge thier behavior values
- Replace the rest with a random number of random genes

##Mutation Algorithm
- Select one random gene and replace it with a random gene

##Fitness Algorithm
`t` time lived (seconds)
`h` damage dealt (hitpoints)
`d` damage recieved (hitpoints)
`k` kills

`fittness = t/20 + 2h + k - d/4`

##For Future Reference

- W `this.theEntity.isInWater()`
- L `this.theEntity.isInLava()`

Reactions
- j Jump/Swim `getJumpHelper().setJumping()`
- p Panic
- r Run (away from target)
- a Attack
- c Charge
- w Wander

-   Avoid Sun `(PathNavigateGround)getNavigator()).setAvoidSun(true)`

search for (water, lava, shade, )


- O opponent (last attacker)
- R within attak range
- H getHealth()/getMaxHealth()
- T has target
- D distance from target
- A can attack
- E (nearby enemies, friends)
- M (is moving, has a path)

- N is night this.theEntity.worldObj.isDaytime()
 List<T> list = this.theEntity.worldObj.<T>getEntitiesWithinAABB(entityClass ...
- F (nearby friends) List<T> list = this.theEntity.worldObj.<T>getEntitiesWithinAABB(entityClass ...
- P (previous attackers) entity.getCombatTracker()

- ? this.theEntity.getItemStackFromSlot(EntityEquipmentSlot.HEAD)
- ? can spawn here
- ? this.theWorld.canSeeSky(new BlockPos(this.theCreature.posX, this.theCreature.getEntityBoundingBox().minY, this.theCreature.posZ))
-  Vec3d vec3d = RandomPositionGenerator.findRandomTarget(this.entity, 10, 7);
- this.theCreature.getBlockPathWeight(blockpos1) < 0.0F)

