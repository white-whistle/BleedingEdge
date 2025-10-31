<img width="200" height="200" src="https://github.com/white-whistle/BleedingEdge/blob/main/src/main/resources/assets/bleeding-edge/icon.png?raw=true" />

# Bleeding Edge
<p>
<a href="https://www.curseforge.com/minecraft/mc-mods/bleeding-edge">
  <img alt="Static Badge" src="https://img.shields.io/badge/CurseForge-orange">
</a>
</p>
a futuristic minecraft gadget mod (Curseforge Modjam 2025 submission)

this mod aims to add interesting trinkets with futuristic look and feel. meant to compliment and complicate PVP gameplay.

most of the items either have a direct counter item (invis vs IR vision), or general counter - EMP (disables electronic gadgets)

<img width="656" height="427" alt="image" src="https://github.com/user-attachments/assets/aececdf7-a7ab-4886-9c31-6fd1d1cc0b30" />

## Dependencies
this mod depends on `Trinkets`, `Satin`, and `PlayerAbilityLib`

## Progression
to begin using this mod, you will need to first smelt some coal into `Carbon Ingot`s
most of the mod's content uses carbon so you better stock up on a bunch
<p>
  <img width="341" height="223" alt="image" src="https://github.com/user-attachments/assets/81be8462-1647-46fa-80d2-c71a84db01ce" />
</p>

the next item you would want to work towards is the `Brain Extractor`, which is used to.. extract brains.
use this item on an entity to begin extracting it's brain.
entities killed by `Brain Damage` will reward the player with their brains.
<p>
  <img width="417" height="573" alt="image" src="https://github.com/user-attachments/assets/33d7ac53-91f2-4152-ad00-14f6bbc2a094" />
</p>
<p>
  <img src="https://github.com/white-whistle/BleedingEdge/blob/main/raw/brain-drill-demo.gif?raw=true" />
</p>

* Players and Neutrals - Small Brain
* Hostiles - Rotten Brain
* Villages - Smart Brain (are not willing to donate their brains unfortunately, you will need to **weaken** them beforehand)

place your newly accuired brains inside a `Brain Jar`, feed the brain with any food item and it will begin crunching some numbers.
when the brain finishes thinking, it will manifest `Eureka`!
<p align="left">
<img width="222" height="149" alt="image" src="https://github.com/user-attachments/assets/55c51b63-19b5-4ac7-b6f3-01e97af848e8" />
</p>
<p>
<img alt="image" src="https://github.com/white-whistle/BleedingEdge/blob/main/raw/brain-jar-demo.gif?raw=true" />
</p>

use Eureka to create `Technology` past your understanding
<p align="left">
<img width="364" height="225" alt="image" src="https://github.com/user-attachments/assets/c3c20fa7-c818-45fd-a5d9-5eec396645da" />
</p>

we can start making some cool stuff now that we gained access to Technology
but our clumsy hands cannot craft items as such precision, for that we need `Basic Slot`s and the `Assembly Slot`
this gives us access to `Assembler Crafting` recipes.
<p>
  <img width="222" height="147" alt="image" src="https://github.com/user-attachments/assets/d3bf4d84-6129-4b0b-8481-c9f7a4bae843" />
</p>

### Slot items
bleeding edge likes inventory interactions (a lot), so its time to get to know `Slot Item`s!
you can click a single item into a slot item by hovering over it with a held stack - and right clicking over it.
to take an a single item out - right click the slot with no held stack.
if you want to insert an entire stack or alternatively empty the contents of a slot - do the same but this time hold the slot and click over a stack / empty inventory slot
<p>
  <img src="https://github.com/white-whistle/BleedingEdge/blob/main/raw/new-slot-look.gif?raw=true" />
</p>

### Assembler Crafting
Crafting with the assembly slot is easy, we first need to arrange our crafting grid using basic slots - make sure there are no stray slots anywhere messing with the assembler (the assembly slot looks for any basic slot item in the inventory it is stored)
Right Click items in/out of basic slots to define the recipe, once everything is in place
Right Click the assembly slot, if the recipe is valid your item will be crafted immediately and the items in the basic slots will get deducted, if the recipe is invalid it will make an angry sound..
<p>
  <img src="https://github.com/white-whistle/BleedingEdge/blob/main/raw/assembler-demo.gif?raw=true" />
</p>

### Danger
Be careful though, each gadget gives you `Threat Score`, when some entities spawn - they will try to match your threat score (scales with difficulty)
<p>
<img width="993" height="714" alt="image" src="https://github.com/user-attachments/assets/eb7712df-756f-4cbb-80d7-7c76c4f4b198" />
</p>

### Toggled Items
some items need to be enabled to give the player their benefits, to toggle an item's state - either invoke its bound keybind or simply click on the stack
<p>
  <img src="https://github.com/white-whistle/BleedingEdge/blob/main/raw/toggled-item-demo.gif?raw=true" />
</p>

### Assembler Recipes

#### Nutrient Block
Not very tasty, but packed with nutrients.

a great efficient food source that takes less time to consume than standard food sources of matching hunger values. Brains love this stuff

<p align="left">
<img width="295" height="157" alt="image" src="https://github.com/user-attachments/assets/9cd91248-f1d1-4b35-9bbc-4e7de9c331d6" />
</p>

#### Heating Slot
a `Slot Item`. items placed inside this slot will be smelted and popped right into the players inventory (or the world if full).
can also be used as an infinite fuel source in furnaces / generators. but it only smelts for 1 burn tick per tick.

<p align="left">
<img width="365" height="225" alt="image" src="https://github.com/user-attachments/assets/9b9f9389-1d3c-4457-9930-40307de502c9" />
</p>

#### Repair Slot
a `Slot Item`. damaged items placed inside this slot will be repaired.

<p align="left">
<img width="362" height="220" alt="image" src="https://github.com/user-attachments/assets/a0ae1174-ed38-4d73-af14-5fe8e90d5c9b" />
</p>

#### Breach Hammer
A melee hammer that is charged like a crossbow (does not require ammo). a charged breach hammer attack is known to bash most heads in (tldr, it deals a lot of damage).
This weapon is so dangerous when charged - a held charged breach hammer will raise your threat score (while the uncharged hammer will not)

<p align="left">
<img width="361" height="294" alt="image" src="https://github.com/user-attachments/assets/1e56fbea-6e13-4e67-831c-3b9cd8c8c018" />
</p>

#### Steel Kidney
While active - grants the user the `Antidote` status. effectively nullifying any posion damage that meets its user.

<p align="left">
<img width="434" height="291" alt="image" src="https://github.com/user-attachments/assets/ac413edc-a670-4e3d-b29a-e8c4dfffe085" />
</p>

#### Liquid Cooling
Utilizing a captive snow golem, When active this gadget keeps its user in a comfortable temperature, even while taking a lava bath or personally attending burning person. (fire resistance)
<p align="left">
<img width="436" height="291" alt="image" src="https://github.com/user-attachments/assets/685d5756-aca1-4cee-871c-654b3ca7ea3e" />
</p>

#### Threat Visor
When active - allows you to see the gadget threat level of entities (yourself included).

<p align="left">
<img width="434" height="290" alt="image" src="https://github.com/user-attachments/assets/5eda812b-5247-4956-9ebd-bfe83cb9976a" />
</p>

#### Night Vision Goggles
When active, it gives the user `IR vision` - a monochrome version of the night vision buff, The key difference - this allows you to see invisible entities normally. a great counter to an unsuspecting `Cloaking Device` user

<p align="left">
<img width="433" height="292" alt="image" src="https://github.com/user-attachments/assets/db7013fc-3927-4e59-94b8-0bf0b593dc62" />
</p>

#### Obsidian Sash
Apparently wearing an obsidian ring around your waist is a great way to carry around a nether portal. When this item is activated (via a bound hotkey) it will teleport toggle the player between the overworld/nether dimension (will malfunction if the user is in neither of those). the teleportation is not instant, and can be cancelled by activating again during the teleportation phase (denoted by the `Quantum Tunneling` status effect).
Great item for escaping combat / ambushing, or just fast travel through the nether.
<p align="left">
<img width="434" height="292" alt="image" src="https://github.com/user-attachments/assets/9d45a3a0-2dec-44e3-b6e3-f8702c9ea0b1" />
</p>

#### Steel Brain
Thinks faster than a smart brain.
Can be consumed to augment its user with a `head/core` slot.
Can also be extracted from the user using brain damage.

<p align="left">
<img width="503" height="362" alt="image" src="https://github.com/user-attachments/assets/8fb7a25a-c7a6-4554-93ce-79fe3e3ddab4" />
</p>

#### Shield Generator
While active and out of combat (have not received damage for 5 seconds), this gadget will grant the user 5 absorption hearts.

<p align="left">
<img width="506" height="362" alt="image" src="https://github.com/user-attachments/assets/8cecdc61-a88a-48b9-84aa-f4b2dc7eadc9" />
</p>

#### Hover Pack
While active, grants the user creative flight capabilities. However, being struck with EMP while in mid air will disable the flight as well as the fall damage protection - this can hurt a bit

<p align="left">
<img width="506" height="363" alt="image" src="https://github.com/user-attachments/assets/96491045-7993-45ab-93da-ef2a9f0a3e2e" />
</p>

#### Storm Bender
This bow conducts lightning through the user and fires an EMP beam, this beam deals lightning damage. direct hits deal more damage than the AOE blast near the impact point.
All hit entities (user included) are electrecuted by this process and suffer the EMP debuff.
This item can malfunction if not fully charged - and will result in an EMP blast around the user.

<p align="left">
<img width="504" height="361" alt="image" src="https://github.com/user-attachments/assets/35a0563d-222b-429d-ba9c-0c6b40699e53" />
</p>

#### Cloaking Device
When active, grants the user the `Cloaked` buff, this causes the user to be completely invisible (armor included). this status also does not emit particle effects.
Cloaked entities can still be seen using IR vision.

<p align="left">
<img width="504" height="364" alt="image" src="https://github.com/user-attachments/assets/fb317943-32b1-41c8-a0d8-e587f715e912" />
</p>

#### Tesla Pack
When active, will create an EMP blast around the user, damaging and debuffing entities in a 5 block radius. the user takes half damage.
This item will try to fire as fast as it can but unfortunately this technology is also affected by EMP resulting in a balanced cooldown between EMP bursts.

<p align="left">
<img width="510" height="366" alt="image" src="https://github.com/user-attachments/assets/c2988294-9026-4b60-90d1-d890db6ff3a9" />
</p>

#### Kevlar Totem
This innovative technology redesigns how totems are used, while it is no longer effective in the hand/offhand slots, it can stop a bullet to the chest pretty well (you wear the totem on your chest).
this gadget meant to help the user recollect himself after a fatal hit, so it also grants it with temporary cloaking. If you spot this gadget on an enemy's chest, hitting him with EMP is a great way to make this item not trigger at deaths door.

<p align="left">
<img width="505" height="365" alt="image" src="https://github.com/user-attachments/assets/981dd239-9d9b-425f-9224-7ab61bc8384d" />
</p>

### Cores
Cores are equippable in the `head/core` slot, to gain access to this slot - consume a `Steel Brain`. this upgrade persists death, if you for some reason want to remove it - dying to brain damage will extract the steel brain.

Cores are a special kind of "companion" gadget that **greatly** buffs the user's stats, and grant additional gadget slots. these items usually have high threat scores.

<p align="left">
<img width="368" height="192" alt="image" src="https://github.com/user-attachments/assets/bea425cb-559d-4dab-b920-44f246819a69" />
</p>
<p align="left">
<img width="507" height="365" alt="image" src="https://github.com/user-attachments/assets/0a0083e8-e397-4ce1-9513-d467141245b1" />
</p>
the standard `TACTICore` can be upgraded to fit different playstyles

#### SPIDERCore
Grants the user more eyes (+ face slots), more legs (+leg slots), move speed, and attack damage.
meant for mobile scout-type playstyle

<p align="left">
<img width="507" height="364" alt="image" src="https://github.com/user-attachments/assets/a5737ad1-1376-4b40-85b4-63827b4ffa11" />
</p>

#### MEDICore
Grants the user more belt and neck slots (so you can resist most status effects), bonus HP, and move speed.
<p align="left">
<img width="504" height="362" alt="image" src="https://github.com/user-attachments/assets/dd4b4884-b389-4ca1-bc5e-f80e78590b24" />
</p>

#### HEAVYCore
Sacrificing mobility and some utility (-neck -belt), grants the user an additional `chest/back` slot, health, armor, and armor toughness.
<p align="left">
<img width="504" height="361" alt="image" src="https://github.com/user-attachments/assets/c1d88497-291d-46ca-81ae-babb2b056386" />
</p>



## Legalities
All rights reserved.
Feel free to use in your modpacks tho







