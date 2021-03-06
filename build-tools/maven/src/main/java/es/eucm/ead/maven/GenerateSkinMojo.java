/**
 * eAdventure is a research project of the
 *    e-UCM research group.
 *
 *    Copyright 2005-2014 e-UCM research group.
 *
 *    You can access a list of all the contributors to eAdventure at:
 *          http://e-adventure.e-ucm.es/contributors
 *
 *    e-UCM is a research group of the Department of Software Engineering
 *          and Artificial Intelligence at the Complutense University of Madrid
 *          (School of Computer Science).
 *
 *          CL Profesor Jose Garcia Santesmases 9,
 *          28040 Madrid (Madrid), Spain.
 *
 *          For more info please visit:  <http://e-adventure.e-ucm.es> or
 *          <http://www.e-ucm.es>
 *
 * ****************************************************************************
 *
 *  This file is part of eAdventure
 *
 *      eAdventure is free software: you can redistribute it and/or modify
 *      it under the terms of the GNU Lesser General Public License as published by
 *      the Free Software Foundation, either version 3 of the License, or
 *      (at your option) any later version.
 *
 *      eAdventure is distributed in the hope that it will be useful,
 *      but WITHOUT ANY WARRANTY; without even the implied warranty of
 *      MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *      GNU Lesser General Public License for more details.
 *
 *      You should have received a copy of the GNU Lesser General Public License
 *      along with eAdventure.  If not, see <http://www.gnu.org/licenses/>.
 */

package es.eucm.ead.maven;

import com.badlogic.gdx.backends.lwjgl.LwjglFiles;
import com.badlogic.gdx.backends.lwjgl.LwjglNativesLoader;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.PixmapPacker;
import com.badlogic.gdx.graphics.g2d.freetype.FreeType;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.tools.bmfont.BitmapFontWriter;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import com.badlogic.gdx.tools.texturepacker.TexturePacker.Settings;
import com.badlogic.gdx.utils.Array;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;

/**
 * The plugin generate a libgdx atlas for the project skins.
 * <p/>
 * To use this plugin, type <strong>one</strong> of the following at the command
 * line:
 * <p/>
 * 
 * <pre>
 * mvn es.e-ucm.ead:ead-maven-plugin:<version (e.g. 0.0.1-SNAPSHOT)>:generate-skins
 * # or
 * mvn es.e-ucm.ead:ead-maven-plugin:generate-skins
 * # or
 * mvn ead:generate-skins
 * </pre>
 * <p/>
 * </code>
 * 
 * @author Ivan Martinez-Ortiz
 */
@Mojo(name = "generate-skins", requiresProject = false, inheritByDefault = false)
public class GenerateSkinMojo extends AbstractMojo {

	private final String defaultPack = "{\n" + "paddingX: 0,\n"
			+ "paddingY: 0,\n" + "filterMin: MipMapLinearNearest,\n"
			+ "filterMag: Linear,\n" + "flattenPaths: true,\n" + "}";
	/** Skin source folder. */
	@Parameter(property = "skins.sourceDir", defaultValue = "${basedir}/assets-raw/skins-raw")
	private File sourceDir;
	/** Generated skin target folder. */
	@Parameter(property = "skins.outputDir", defaultValue = "${basedir}/assets/skins")
	private File outputDir;

	public void execute() throws MojoExecutionException {

		if (!sourceDir.exists()) {
			throw new MojoExecutionException(
					"[generate-skins] Source directory does not exists: "
							+ sourceDir);
		}

		if (!outputDir.exists()) {
			if (!outputDir.mkdir()) {
				throw new MojoExecutionException(
						"[generate-skins] Cannot create output directory: "
								+ outputDir);
			}
		}

		Settings settings = new Settings();
		settings.limitMemory = false;

		LwjglFiles files = new LwjglFiles();
		LwjglNativesLoader.load();
		FileHandle rawRoot = files.absolute(sourceDir.getAbsolutePath());
		FileHandle skinsRoot = new FileHandle(files.internal(
				outputDir.getAbsolutePath()).file());

		getLog().info("[generate-skins] Removing old skins (if any)");
		for (FileHandle folder : skinsRoot.list()) {
			folder.deleteDirectory();
		}

		Array<FileHandle> tempFolders = new Array<FileHandle>();
		for (FileHandle skinRaw : rawRoot.list()) {
			if (skinRaw.isDirectory()) {
				FileHandle skinFolder = skinsRoot.child(skinRaw.name());

				if (!skinFolder.exists()) {

					if (skinRaw.child("common").exists()) {
						generateMultipleSkins(skinRaw, skinsRoot);
					} else {
						getLog().info(
								"[generate-skins] Generating: "
										+ skinRaw.name());
						skinFolder.mkdirs();
						FileHandle imagesFolder = skinRaw.child("images");

						FileHandle fonts = skinRaw.child("ttf2fnt");
						if (fonts.exists()) {
							FileHandle config = fonts.child("fonts.config");
							// fonts.config defines which .ttf will be
							// transformed
							// to what .fnt and with what size.
							if (!config.exists()) {
								fonts.copyTo(skinFolder);
							} else {
								String[] lines = config.readString()
										.split("\n");
								for (String line : lines) {
									String[] props = line.split(" ");
									String ttfFontName = props[0];
									String fontSize = props[1];
									fontSize = fontSize.replace("\r", "");

									FileHandle child = fonts.child(ttfFontName);
									if (child.exists()) {
										String fontName = child
												.nameWithoutExtension()
												+ "-"
												+ fontSize;
										FileHandle dstFolder = imagesFolder
												.child(fontName);
										dstFolder.mkdirs();
										dstFolder
												.child("pack.json")
												.writeString(defaultPack, false);
										tempFolders.add(dstFolder);
										generateFiles(fontName, child,
												Integer.valueOf(fontSize),
												dstFolder);
										dstFolder.child(fontName + ".fnt")
												.copyTo(skinFolder);
									}
								}
							}
						}
						fonts = skinRaw.child("fonts");
						if (fonts.exists()) {
							fonts.copyTo(skinFolder);
						}

						TexturePacker.process(settings, imagesFolder.file()
								.getAbsolutePath(), skinFolder.file()
								.getAbsolutePath(), "skin");

						skinRaw.child("skin.json").copyTo(skinFolder);

						for (FileHandle tempFolder : tempFolders) {
							tempFolder.deleteDirectory();
						}
					}
				} else {
					getLog().info(
							"[generate-skins] skin already exists, skipping: "
									+ skinRaw.name());
				}
			}
		}

	}

	private void generateMultipleSkins(FileHandle skinRaw, FileHandle skinRoot) {
		FileHandle[] ttfFonts = skinRaw.list(".ttf");
		FileHandle common = skinRaw.child("common");
		for (FileHandle dpi : skinRaw.list()) {
			if (dpi.isDirectory() && !"common".equals(dpi.name())
					&& !"svg".equals(dpi.name())
					&& !"9patch".equals(dpi.name())) {
				FileHandle skinFolder = skinRoot.child(skinRaw.name() + '-'
						+ dpi.name());
				skinFolder.mkdirs();
				skinRaw.child("skin.json").copyTo(skinFolder);
				FileHandle atlasImagesFolder = skinFolder.child("atlas");
				atlasImagesFolder.mkdirs();

				Settings settings = new Settings();
				settings.limitMemory = false;
				if (dpi.name().contains("xxx")) {
					settings.maxWidth = 2048;
					settings.maxHeight = 2048;
				}

				common.copyTo(atlasImagesFolder);
				dpi.child("images").copyTo(atlasImagesFolder);
				for (FileHandle font : ttfFonts) {
					font.copyTo(skinFolder);
				}

				TexturePacker.process(settings, atlasImagesFolder.file()
						.getAbsolutePath(),
						skinFolder.file().getAbsolutePath(), "skin");

				atlasImagesFolder.deleteDirectory();
			}
		}
	}

	/**
	 * Convenience method for generating a font, and then writing the fnt and
	 * png files. Writing a generated font to files allows the possibility of
	 * only generating the fonts when they are missing, otherwise loading from a
	 * previously generated file.
	 * 
	 * @param fontFile
	 * @param fontSize
	 * @param destiny
	 */
	private void generateFiles(String fontName, FileHandle fontFile,
			int fontSize, FileHandle destiny) {
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(fontFile);

		// compute the minimum page size for a square png
		FreeType.Library library = FreeType.initFreeType();
		FreeType.Face face = FreeType.newFace(library, fontFile, 0);
		FreeType.setPixelSizes(face, 0, fontSize);
		FreeType.SizeMetrics fontMetrics = face.getSize().getMetrics();
		float scale = FreeType.toInt(fontMetrics.getHeight());

		for (int c = 32; c < (32 + face.getNumGlyphs()); c++) {
			if (FreeType.loadChar(face, c, FreeType.FT_LOAD_DEFAULT)) {
				int lh = FreeType.toInt(face.getGlyph().getMetrics()
						.getHeight());
				scale = (lh > scale) ? lh : scale;
			}
		}

		// generate the glyphs
		int maxGlyphHeight = (int) Math.ceil(scale);
		float area = maxGlyphHeight * maxGlyphHeight
				* FreeTypeFontGenerator.DEFAULT_CHARS.length();
		int pageWidth = MathUtils.nextPowerOfTwo((int) Math.sqrt(area));

		pageWidth = Math.min(pageWidth, 1024);

		PixmapPacker packer = new PixmapPacker(pageWidth, pageWidth,
				Pixmap.Format.RGBA8888, 2, false);

		FreeTypeFontGenerator.FreeTypeFontParameter param = new FreeTypeFontGenerator.FreeTypeFontParameter();
		param.packer = packer;
		param.characters = FreeTypeFontGenerator.DEFAULT_CHARS;
		param.size = fontSize;
		param.flip = false;

		FreeTypeFontGenerator.FreeTypeBitmapFontData fontData = generator
				.generateData(param);

		saveFontToFile(fontData, fontSize, fontName, packer, destiny);
		generator.dispose();
		packer.dispose();
	}

	private void saveFontToFile(
			FreeTypeFontGenerator.FreeTypeBitmapFontData data, int fontSize,
			String fontName, PixmapPacker packer, FileHandle destiny) {
		FileHandle fontFile = destiny.child(fontName + ".fnt"); // .fnt path

		BitmapFontWriter.setOutputFormat(BitmapFontWriter.OutputFormat.Text);

		String[] pageRefs = BitmapFontWriter.writePixmaps(packer.getPages(),
				destiny, fontName); // png dir path

		BitmapFontWriter.writeFont(data, pageRefs, fontFile,
				new BitmapFontWriter.FontInfo(fontName, fontSize), 1, 1);
	}

}
