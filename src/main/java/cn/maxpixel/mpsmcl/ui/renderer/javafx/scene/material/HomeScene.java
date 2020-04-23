/*
 *     Copyright (C) 2019-2020  MaxPixel Studios
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package cn.maxpixel.mpsmcl.ui.renderer.javafx.scene.material;

import cn.maxpixel.mpsmcl.ui.renderer.javafx.scene.Sceneable;
import javafx.scene.Scene;
import javafx.scene.layout.TilePane;

public class HomeScene implements Sceneable {
	private TilePane pane;

	public HomeScene() {
		pane = new TilePane();
	}

	@Override
	public Scene getScene() {
		return new Scene(pane);
	}
}
