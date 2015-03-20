#
# contest_results.rb
# 
# Generated by JSON Model Generator v0.0.4 on Fri Mar 20 08:22:20 MDT 2015.
# https://github.com/intere/generator-json-models
# 
# The generator tool is licensed under the LGPL: http://www.gnu.org/licenses/lgpl-3.0.html#content
#

class ContestResults < JsonModel
	def order
		json.try(:[], :order)
	end

	def sponsor
		@sponsor ||= ContestResultsSponsor.new json.try(:[], :sponsor)
	end

	def track_id
		json.try(:[], :trackId)
	end

	def user_id
		json.try(:[], :userId)
	end

	def winner
		json.try(:[], :winner)
	end


end		# End of ContestResults

