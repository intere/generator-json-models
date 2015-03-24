#
# contest.rb
# 
# Generated by JSON Model Generator v0.0.4 on Tue Mar 24 07:59:17 MDT 2015.
# https://github.com/intere/generator-json-models
# 
# The generator tool is licensed under the LGPL: http://www.gnu.org/licenses/lgpl-3.0.html#content
#

class Contest < JsonModel
	def id
		json.try(:[], :id)
	end

	def title
		json.try(:[], :title)
	end

	def type_name
		json.try(:[], :typeName)
	end

	def state_name
		json.try(:[], :stateName)
	end

	def image_url
		json.try(:[], :imageUrl)
	end

	def description
		json.try(:[], :description)
	end

	def start_date
		json.try(:[], :startDate)
	end

	def end_date
		json.try(:[], :endDate)
	end

	def user_ids
		@user_ids ||= json.try(:[], :userIDs)
	end

	def results
		@results ||= json.try(:[], :results).map {|o| ContestResults.new(results)}
	end

	def created_date
		json.try(:[], :createdDate)
	end

	def latitude
		json.try(:[], :latitude)
	end

	def longitude
		json.try(:[], :longitude)
	end

	def purse_description
		json.try(:[], :purseDescription)
	end

	def purse_image_url
		json.try(:[], :purseImageUrl)
	end

	def purse_prize_value
		json.try(:[], :pursePrizeValue)
	end

	def purse_title
		json.try(:[], :purseTitle)
	end

	def purse_type
		json.try(:[], :purseType)
	end

	def runner_up_description
		json.try(:[], :runnerUpDescription)
	end

	def runner_up_prize_value
		json.try(:[], :runnerUpPrizeValue)
	end

	def runner_up_purse_image_url
		json.try(:[], :runnerUpPurseImageUrl)
	end

	def runner_up_title
		json.try(:[], :runnerUpTitle)
	end

	def scored
		json.try(:[], :scored)
	end

	def size
		json.try(:[], :size)
	end

	def sponsor
		@sponsor ||= ContestSponsor.new json.try(:[], :sponsor)
	end

	def state
		json.try(:[], :state)
	end

	def track_ids
		@track_ids ||= json.try(:[], :trackIDs)
	end

	def type
		json.try(:[], :type)
	end


end		# End of Contest

