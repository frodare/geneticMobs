# DNA Description / Notes

##Global Parameters
`biopoints` 255

- Install the genetic AI task in target mobs
- Save spawn time on initial spawn
- Only allow mobs to live for 30s
- When a mob dies, breed a new one
- Keep a fitness table updated (with current max fittness)

##Environmental Factors
- Heath `0-256`
- Target Distance `0-256`
- Has Target `0-1`
- Enemies Nearby `0-1`
- Can Attack `0-1`
- Is Moving `0-1`

##Stimuli
- `0` Tick (will not trigger durning running task)
- `1` On Take Damage
- `2` See Entity
- `3` Is Burning
- `4` On Start Attack
- `5` On Collide into Opponent

##Actions
- `0` Jump/Swim
- `1` Panic (Time, 0 - 10)
- `2` Run Away From Target (Time, 0 - 10)
- `3` Attack Target
- `4` Charge Target (Time, 0 - 10)
- `5` Wander (Time)
- `6` Strafe (Time)
- `7` Set Target (`0` Last Attacker, `1` Random Opponent)

##DNA Format
- `long 0` Traits
- `long 1 - n` Behavoirs

##Traits Gene Byte Format (single java long)
- `byte 0` DNA version
- `byte 1` Strength (2-5 hit points)
- `byte 2` Speed (0.5-1.5 m/s)
- `byte 3` Life Span (10-40 seconds)
- `byte 4` Health (6-13) 
- `byte 5` Regeneration (1-4)
- `byte 6`
- `byte 7`

All trait parameters are nomalized to `0 - 255`.
Normalized trait values must add up to `biopoints`.

###Normalize Traits Gene
1. Zero the last two bytes
2. Calculate sum
3. Subtract or add from all traits until within six points of the `biopoints`
4. Choose random trait to add or subtract from, repeat until sum equals the `biopoints`

##Behavoir Gene Byte Format (single java long)
Gene Type
- `byte 0`
	+ `nibble 0` Stimulus Code
	+ `nibble 1` Action Code

- `byte 1` Action parameter

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

By Concatenation and Removal
- Concatenate both gene sequences 
- Choose a random number of genes to remove between 1/4 to 3/4

By Gene Averaging
- Keep all common genes (same first byte) and averge thier behavior values
- Replace the rest with a random number of random genes

##Mutation Algorithm
- Select one random gene and replace it with a random gene

##Fitness Algorithm
`t` time lived (seconds)

`h` damage dealt (hitpoints)

`d` damage recieved (hitpoints)

`k` kills

`fittness = t/10 + 2h + k - d/4`

##Selection Algorithm
- Simply pick the best two?
- pick a parent randomly, keep if normlized fitness is greater than a random number

##For Future Reference

- W `this.theEntity.isInWater()`
- L `this.theEntity.isInLava()`

- Before block change

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

