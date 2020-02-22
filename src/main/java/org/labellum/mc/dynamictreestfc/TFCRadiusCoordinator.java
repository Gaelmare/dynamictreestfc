package org.labellum.mc.dynamictreestfc;

import java.util.Random;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.NoiseGeneratorPerlin;

import com.ferreusveritas.dynamictrees.api.worldgen.IRadiusCoordinator;
import com.ferreusveritas.dynamictrees.worldgen.TreeGenerator;
import net.dries007.tfc.world.classic.chunkdata.ChunkDataProvider;
import net.dries007.tfc.world.classic.chunkdata.ChunkDataTFC;

public class TFCRadiusCoordinator implements IRadiusCoordinator
{
    public NoiseGeneratorPerlin noiseGenerator;
    protected final TreeGenerator treeGenerator;
    protected final World world;

    public TFCRadiusCoordinator(TreeGenerator treeGenerator, World world) {
        noiseGenerator = new NoiseGeneratorPerlin(new Random(96), 1);
        this.world = world;
        this.treeGenerator = treeGenerator;
    }

    @Override
    public int getRadiusAtCoords(int x, int z) {

        double scale = 128;//Effectively scales up the noisemap
        double noiseDensity = noiseGenerator.getValue(x / scale, z / scale);//Gives -1.0 to 1.0
        Chunk chunk = world.getChunk(new BlockPos(x + 8, 0, z + 8));
        ChunkDataTFC chunkData = (ChunkDataTFC)chunk.getCapability(ChunkDataProvider.CHUNK_DATA_CAPABILITY, null);
        double density;
        if ( chunkData != null)
        {
            density = chunkData.getFloraDensity();
        }
        else
        {
            density = 0.5;
        }
        density += world.rand.nextFloat()*noiseDensity;

        double size = ((1.0 - density) * 9);//Size is the inverse of density(Gives 0 to 9)

        //Oh Joy. Random can potentially start with the same number for each chunk. Let's just
        //throw this large prime xor hack in there to get it to at least look like it's random.
        int kindaRandom = ((x * 674365771) ^ (z * 254326997)) >> 4;
        int shakelow =  (kindaRandom & 0x3) % 3;//Produces 0,0,1 or 2
        int shakehigh = (kindaRandom & 0xc) % 3;//Produces 0,0,1 or 2

        return MathHelper.clamp((int) size, 2 + shakelow, 8 - shakehigh);//Clamp to tree volume radius range
    }

    @Override
    public boolean runPass(int chunkX, int chunkZ, int pass) {
        return pass == 0;
    }
}

