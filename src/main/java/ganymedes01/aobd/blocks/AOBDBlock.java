package ganymedes01.aobd.blocks;

import ganymedes01.aobd.AOBD;
import ganymedes01.aobd.lib.Reference;
import ganymedes01.aobd.ore.Ore;
import ganymedes01.aobd.recipes.modules.ExNihilo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.StatCollector;
import net.minecraft.world.IBlockAccess;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class AOBDBlock extends Block {

	public static final List<AOBDBlock> ALL_BLOCKS = new ArrayList<AOBDBlock>();
	public static final Map<String, Float> BLOCKS_PREFIXES = new HashMap<String, Float>();
	public static final Map<String, Material> BLOCKS_MATERIALS = new HashMap<String, Material>();
	public static final Map<String, Block.SoundType> BLOCKS_SOUNDS = new HashMap<String, Block.SoundType>();
	public static final Map<String, String[]> BLOCKS_EXNIHILO = new HashMap<String, String[]>();
	static {
		BLOCKS_PREFIXES.put("block", 4F);
		BLOCKS_PREFIXES.put("oreSand", 0.4F);
		BLOCKS_PREFIXES.put("oreDust", 0.6F);
		BLOCKS_PREFIXES.put("oreGravel", 0.8F);
		BLOCKS_PREFIXES.put("oreNetherGravel", 0.8F);

		BLOCKS_MATERIALS.put("block", Material.iron);
		BLOCKS_MATERIALS.put("oreSand", Material.sand);
		BLOCKS_MATERIALS.put("oreDust", Material.sand);
		BLOCKS_MATERIALS.put("oreGravel", Material.sand);
		BLOCKS_MATERIALS.put("oreNetherGravel", Material.sand);

		BLOCKS_SOUNDS.put("block", Block.soundTypeMetal);
		BLOCKS_SOUNDS.put("oreSand", Block.soundTypeSand);
		BLOCKS_SOUNDS.put("oreDust", Block.soundTypeSand);
		BLOCKS_SOUNDS.put("oreGravel", Block.soundTypeGravel);
		BLOCKS_SOUNDS.put("oreNetherGravel", Block.soundTypeGravel);

		BLOCKS_EXNIHILO.put("oreSand", new String[] { "IconSandBase", "IconSandTemplate" });
		BLOCKS_EXNIHILO.put("oreDust", new String[] { "IconDustBase", "IconDustTemplate" });
		BLOCKS_EXNIHILO.put("oreGravel", new String[] { "IconGravelBase", "IconGravelTemplate" });
		BLOCKS_EXNIHILO.put("oreNetherGravel", new String[] { "IconGravelBaseNether", "IconGravelTemplate" });
	}

	protected final Ore ore;
	protected final String base;

	public AOBDBlock(String base, Ore ore) {
		this(BLOCKS_MATERIALS.get(base), base, ore);
	}

	public AOBDBlock(Material material, String base, Ore ore) {
		super(material);
		this.ore = ore;
		this.base = base;

		Float hardness = BLOCKS_PREFIXES.get(base);
		setHardness(hardness == null ? 1.0F : hardness);

		setStepSound(BLOCKS_SOUNDS.get(base));

		setCreativeTab(AOBD.tab);
		setBlockName(Reference.MOD_ID + "." + base + ore);
		setBlockTextureName(Reference.MOD_ID + ":" + base);
		ALL_BLOCKS.add(this);
	}

	protected String getFullName() {
		return "tile." + Reference.MOD_ID + "." + base + ore.name() + ".name";
	}

	protected String getShortName() {
		return "tile." + Reference.MOD_ID + "." + base + ".name";
	}

	public String getBaseName() {
		return base;
	}

	public Ore getOre() {
		return ore;
	}

	@Override
	public String getLocalizedName() {
		String fullName = getFullName();
		String shortName = getShortName();
		return StatCollector.canTranslate(fullName) ? StatCollector.translateToLocal(fullName) : String.format(StatCollector.translateToLocal(shortName), ore.translatedName());
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister reg) {
		String[] type = BLOCKS_EXNIHILO.get(base);
		if (type != null) {
			TextureMap map = (TextureMap) reg;
			TextureAtlasSprite texture = ExNihilo.createIcon(this, type[0], type[1], map);

			TextureAtlasSprite existing = map.getTextureExtry(texture.getIconName());
			if (existing == null) {
				boolean success = map.setTextureEntry(texture.getIconName(), texture);
				if (success)
					blockIcon = map.getTextureExtry(texture.getIconName());
			}
		} else
			super.registerBlockIcons(reg);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int colorMultiplier(IBlockAccess world, int x, int y, int z) {
		return BLOCKS_EXNIHILO.containsKey(base) ? super.colorMultiplier(world, x, y, z) : ore.colour();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getRenderColor(int meta) {
		return BLOCKS_EXNIHILO.containsKey(base) ? super.getRenderColor(meta) : ore.colour();
	}
}