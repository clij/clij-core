// adapted from: https://github.com/maweigert/gputools/blob/master/gputools/transforms/kernels/transformations.cl
//
// Copyright (c) 2016, Martin Weigert
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted provided that the following conditions are met:
//
// * Redistributions of source code must retain the above copyright notice, this
//   list of conditions and the following disclaimer.
//
// * Redistributions in binary form must reproduce the above copyright notice,
//   this list of conditions and the following disclaimer in the documentation
//   and/or other materials provided with the distribution.
//
// * Neither the name of gputools nor the names of its
//   contributors may be used to endorse or promote products derived from
//   this software without specific prior written permission.
//
// THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
// AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
// IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
// DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
// FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
// DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
// SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
// CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
// OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
// OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

// Adapted from net.haesleinhuepf.clij.kernels.affine_interpolate.cl
// by: @phaub
// July 2019


#ifndef SAMPLER_FILTER
#define SAMPLER_FILTER CLK_FILTER_LINEAR
#endif

#ifndef SAMPLER_ADDRESS
#define SAMPLER_ADDRESS CLK_ADDRESS_CLAMP
#endif

__kernel void affine_2D(DTYPE_IMAGE_IN_2D input,
	      			 DTYPE_IMAGE_OUT_2D output,
				 __constant float * mat)
{

  const sampler_t sampler = CLK_NORMALIZED_COORDS_TRUE|
      SAMPLER_ADDRESS |	SAMPLER_FILTER;

  uint i = get_global_id(0);
  uint j = get_global_id(1);

  uint Nx = GET_IMAGE_WIDTH(input);
  uint Ny = GET_IMAGE_HEIGHT(input);

  float x = i+0.5f;
  float y = j+0.5f;

  float y2 = (mat[3]*x+mat[4]*y+mat[5]);
  float x2 = (mat[0]*x+mat[1]*y+mat[2]);

  int2 coord_norm = (int2)(x2,y2);

  float pix = 0;
  if (x2 >= 0 && y2 >= 0 &&
        x2 < GET_IMAGE_WIDTH(input) && y2 < GET_IMAGE_HEIGHT(input)
    ) {
    pix = (float)(READ_IMAGE_2D(input, sampler, coord_norm).x);
  }

  int2 pos = (int2){i, j};

  WRITE_IMAGE_2D(output, pos, (DTYPE_OUT) CONVERT_DTYPE_OUT(pix));
  
}